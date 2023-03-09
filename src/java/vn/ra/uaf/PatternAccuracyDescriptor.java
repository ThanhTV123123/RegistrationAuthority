package vn.ra.uaf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatternAccuracyDescriptor {
	final static Logger logger = Logger.getLogger(PatternAccuracyDescriptor.class);
	
    private short blockSlowdown;
    private short maxRetries;
    private long minComplexity;

    public static PatternAccuracyDescriptor fromJSON(String json) {
        try {
            return new ObjectMapper().readValue(json, PatternAccuracyDescriptor.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public short getBlockSlowdown() {
        return this.blockSlowdown;
    }

    public short getMaxRetries() {
        return this.maxRetries;
    }

    public long getMinComplexity() {
        return this.minComplexity;
    }

    public void setBlockSlowdown(short blockSlowdown) {
        this.blockSlowdown = blockSlowdown;
    }

    public void setMaxRetries(short maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setMinComplexity(long minComplexity) {
        this.minComplexity = minComplexity;
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
        return "PatternAccuracyDescriptor [minComplexity=" + this.minComplexity + ", maxRetries=" + this.maxRetries + ", blockSlowdown=" + this.blockSlowdown + "]";
    }
}
