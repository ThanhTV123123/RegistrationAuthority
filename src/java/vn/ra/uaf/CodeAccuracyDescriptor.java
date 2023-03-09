package vn.ra.uaf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CodeAccuracyDescriptor {
	final static Logger logger = Logger.getLogger(CodeAccuracyDescriptor.class);
	
    private short base;
    private short blockSlowdown;
    private short maxRetries;
    private short minLength;

    public static CodeAccuracyDescriptor fromJSON(String json){
        try {
            return new ObjectMapper().readValue(json, CodeAccuracyDescriptor.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.equals(e.getMessage());
        }
        return null;
    }

    public short getBase() {
        return this.base;
    }

    public short getBlockSlowdown() {
        return this.blockSlowdown;
    }

    public short getMaxRetries() {
        return this.maxRetries;
    }

    public short getMinLength() {
        return this.minLength;
    }

    public void setBase(short base) {
        this.base = base;
    }

    public void setBlockSlowdown(short blockSlowdown) {
        this.blockSlowdown = blockSlowdown;
    }

    public void setMaxRetries(short maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setMinLength(short minLength) {
        this.minLength = minLength;
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
        return "CodeAccuracyDescriptor [base=" + this.base + ", minLength=" + this.minLength + ", mexRetries=" + this.maxRetries + ", blockSlowdown=" + this.blockSlowdown + "]";
    }
}
