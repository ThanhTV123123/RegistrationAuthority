package vn.ra.uaf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class rgbPalletteEntry {
	final static Logger logger = Logger.getLogger(rgbPalletteEntry.class);
	
    private short r;
    private short g;
    private short b;

    public static rgbPalletteEntry fromJSON(String json) {
        try {
            return new ObjectMapper().readValue(json, rgbPalletteEntry.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

    public short getB() {
        return this.b;
    }

    public short getG() {
        return this.g;
    }

    public short getR() {
        return this.r;
    }

    public void setB(short b) {
        this.b = b;
    }

    public void setG(short g) {
        this.g = g;
    }

    public void setR(short r) {
        this.r = r;
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
        return "rgbPalletteEntry [r=" + this.r + ", g=" + this.g + ", b=" + this.b + "]";
    }
}
