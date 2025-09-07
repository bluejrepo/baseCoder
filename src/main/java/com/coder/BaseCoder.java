package com.coder;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

/**
 * A comprehensive base encoding/decoding utility that supports various number bases
 * including octal (base-8), hexadecimal (base-16), base32, and custom bases.
 * 
 * This implementation handles large inputs efficiently and provides methods for
 * both encoding and decoding operations.
 */
public class BaseCoder {
    
    public enum EncodingType {
        OCTAL(8);
        
        private final int base;
        
        EncodingType(int base) {
            this.base = base;
        }
        
        public int getBase() {
            return base;
        }
    }
     
    /**
     * Decodes a string in the specified base to an array of bytes in decimal form.
     * 
     * @param encodedString The encoded string to decode
     * @param encodingType The encoding type (base) to use for decoding
     * @return Array of bytes in decimal form
     * @throws IllegalArgumentException if the input contains invalid digits for the specified base
     */
    public static int[] decodeToBytes(String encodedString, EncodingType encodingType) {
        if(StringUtils.isBlank(encodedString)) return new int[0];
        
        validateInput(encodedString, encodingType);
        
        // Convert the encoded string to a BigInteger
        BigInteger bigInt = new BigInteger(encodedString, encodingType.getBase());
        // Convert BigInteger to byte array
        byte[] byteArray = bigInt.toByteArray();
        
        // Handle the case where BigInteger.toByteArray() adds an extra sign byte
        // When the most significant bit is 1 (like with value 255 = 0xFF), BigInteger
        // adds a leading 0 byte to indicate it's positive. We need to remove this.
        // If the first byte is 0 and we have more than one byte, it's likely a sign byte
        int startIndex = 0;
        if (byteArray.length > 1 && byteArray[0] == 0) {
            startIndex = 1;
        }
        
        // Convert bytes to int array with decimal values
        int[] result = new int[byteArray.length - startIndex];
        for (int i = 0; i < result.length; i++) {
            // Convert signed byte to unsigned int (0-255)
            result[i] = Byte.toUnsignedInt(byteArray[i + startIndex]);
        }
       
        return result;
    }

    
    /**
     * Encodes an array of bytes (in decimal form) to a string in the specified base.
     * 
     * @param bytes Array of bytes in decimal form
     * @param encodingType The encoding type (base) to use for encoding
     * @return Encoded string representation
     * @throws IllegalArgumentException if any byte value is outside 0-255 range
     */
    public static String encodeBytes(int[] bytes, EncodingType encodingType) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        
        validateByteArray(bytes);
        
        // Convert int array to byte array
        byte[] byteArray = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byteArray[i] = (byte) bytes[i];
        }
        
        // Convert byte array to BigInteger (treating as positive)
        BigInteger bigInt = new BigInteger(1, byteArray);
        
        // Convert to the specified base
        return bigInt.toString(encodingType.getBase()).toUpperCase();
    }
    
  
    /**
     * Validates that all values in the byte array are within the valid byte range (0-255).
     */
    private static void validateByteArray(int[] bytes) {
    	//Arrays.stream(bytes)
    	//	  .filter(e -> (e > 255) || (e < 0) )
    	//	  .findAny()
    	//	  .ifPresent(b -> {throw new IllegalArgumentException("Byte value " + b + " is outside valid range 0-255"); });
    	
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0 || bytes[i] > 255) {
                throw new IllegalArgumentException("Byte value at index " + i + " (" + bytes[i] + ") is outside valid range 0-255");
            }
        }
    }
    
    /**
     * Validates that the input string contains only valid digits for the specified base.
     */
    private static void validateInput(String input, EncodingType encodingType) {
        String validChars;
        switch (encodingType) {
            case OCTAL:
                validChars = "01234567";
                break;
            default:
                return; // Skip validation for unknown types
        }
        
        for (char c : input.toCharArray()) {
            if (validChars.indexOf(c) == -1) {
                throw new IllegalArgumentException("Invalid character '" + c + "' for " + encodingType + " encoding");
            }
        }
    }
}
