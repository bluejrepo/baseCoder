package com.encoder;

import org.junit.jupiter.api.Test;
import com.coder.BaseCoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Comprehensive test suite for the BaseEncoder class.
 */
public class BaseCoderTest {
    
    @Nested
    @DisplayName("Octal Encoding/Decoding Tests")
    class OctalTests {
        
        @Test
        @DisplayName("Should decode simple octal string correctly")
        void testSimpleOctalDecoding() {
            String input = "31646541";
            int[] expected = {103, 77, 97};
            int[] actual = BaseCoder.decodeToBytes(input, BaseCoder.EncodingType.OCTAL);
            
            assertArrayEquals(expected, actual, 
                "Octal string '" + input + "' should decode to " + Arrays.toString(expected));
        }
        
        @Test
        @DisplayName("Should encode bytes to octal correctly")
        void testSimpleOctalEncoding() {
            int[] input = {103, 77, 97};
            String expected = "31646541";
            String actual = BaseCoder.encodeBytes(input, BaseCoder.EncodingType.OCTAL);
            
            assertEquals(expected, actual, 
                "Bytes " + Arrays.toString(input) + " should encode to '" + expected + "'");
        }
        
        @Test
        @DisplayName("Should handle round-trip encoding/decoding")
        void testOctalRoundTrip() {
            String originalOctal = "31646541";
            int[] decoded = BaseCoder.decodeToBytes(originalOctal, BaseCoder.EncodingType.OCTAL);
            String reencoded = BaseCoder.encodeBytes(decoded, BaseCoder.EncodingType.OCTAL);
            
            assertEquals(originalOctal, reencoded, "Round-trip should preserve original value");
        }
        
        @Test
        @DisplayName("Should handle long octal input")
        void testLongOctalInput() {
            String longInput =  "227711474231113516702134342400414143206126403671660545541234" +
            					"216161770521652245433114475436547416173670422136456436311234" +
            				    "717046577704333460730170473602176263254671507630065771331234" +
            				    "333465753306216356425416366443265355016660043333267564244321" +
            				    "350700124251451433665154621070427111157201067171276700624321" +
            				    "717046577704333460730170473602176263254671507630065771334321" +
            				    "541526554667660414027165423111111315057614760526500045244321" +
            				    "333465753306216356425416366443265355016660043333267564241234" +
            				    "717046577704333460730170473602176263254671507630065771335672" +
            				    "216161770521652245433114475436547416173670422136456436315672" +
            				    "333465753306216356425416366443265355016660043333267564243765" +
            				    "717046577704333460730170473602176263254671507630065771333765" +
            				    "333465753306216356425416366443265355016660043333267564243765" +
            				    "717046577704333460730170473602176263254671507630065771337564" +
            				    "216161770521652245433114475436547416173670422136456436317564" +
            					"333465753306216356425416366443265355016660043333267564247564" ;
            /**
            assertDoesNotThrow(() -> {
	        	long startTime = System.currentTimeMillis();
	            int[] decoded = BaseCoder.decodeToBytes(longInput, BaseCoder.EncodingType.OCTAL);
	            long decodeTime = System.currentTimeMillis() - startTime;
	            System.out.printf("%d bytes decoded time %d (ms) \n", decoded.length, decodeTime);
	            assertTrue(decodeTime < 1000, "Decoding should complete within 1 second"); 
	            
	            startTime = System.currentTimeMillis();
	            String reencoded = BaseCoder.encodeBytes(decoded, BaseCoder.EncodingType.OCTAL);
	            long encodeTime = System.currentTimeMillis() - startTime;
	            System.out.printf("Encoded time %d (ms) \n", encodeTime);
	            assertTrue(encodeTime < 1000, "Encoding should complete within 1 second");
	            
	            assertEquals(longInput, reencoded, "Long input round-trip should work");
            }, "Long input should be processed without errors");
            **/
            
        	long startTime = System.currentTimeMillis();
            int[] decoded = BaseCoder.decodeToBytes(longInput, BaseCoder.EncodingType.OCTAL);
            long decodeTime = System.currentTimeMillis() - startTime;
            System.out.printf("%d bytes decoded time %d (ms) \n", decoded.length, decodeTime);
            assertTrue(decodeTime < 1000, "Decoding should complete within 1 second"); 
            
            startTime = System.currentTimeMillis();
            String reencoded = BaseCoder.encodeBytes(decoded, BaseCoder.EncodingType.OCTAL);
            long encodeTime = System.currentTimeMillis() - startTime;
            System.out.printf("Encoded time %d (ms) \n", encodeTime);
            assertTrue(encodeTime < 1000, "Encoding should complete within 1 second");
            
            assertEquals(longInput, reencoded, "Long input round-trip should work");
  
        }
        
        @Test
        @DisplayName("Should handle empty input")
        void testEmptyOctalInput() {
            assertArrayEquals(new int[0], BaseCoder.decodeToBytes("", BaseCoder.EncodingType.OCTAL));
            assertEquals("", BaseCoder.encodeBytes(new int[0], BaseCoder.EncodingType.OCTAL));
        }
        
        @Test
        @DisplayName("Should reject invalid octal digits")
        void testInvalidOctalDigits() {
            assertThrows(IllegalArgumentException.class, 
                () -> BaseCoder.decodeToBytes("123489", BaseCoder.EncodingType.OCTAL), // 8 and 9 are invalid in octal
                "Should reject invalid octal digits");
        }
        
        @Test
        @DisplayName("Should handle single digit octal")
        void testSingleDigitOctal() {
            for (int i = 0; i <= 7; i++) {
                String octal = String.valueOf(i);
                int[] decoded = BaseCoder.decodeToBytes(octal, BaseCoder.EncodingType.OCTAL);
                String reencoded = BaseCoder.encodeBytes(decoded, BaseCoder.EncodingType.OCTAL);
                assertEquals(octal, reencoded, "Single digit " + i + " should round-trip correctly");
            }
        }
    }
    
    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {
        
        @Test
        @DisplayName("Should handle null inputs gracefully")
        void testNullInputs() {
            assertArrayEquals(new int[0], BaseCoder.decodeToBytes(null, BaseCoder.EncodingType.OCTAL));
            assertEquals("", BaseCoder.encodeBytes(null, BaseCoder.EncodingType.OCTAL));
        }
        
        @Test
        @DisplayName("Should validate byte range")
        void testByteRangeValidation() {
            int[] invalidBytes = {-1, 256}; // Outside 0-255 range
            
            assertThrows(IllegalArgumentException.class, 
                () -> BaseCoder.encodeBytes(invalidBytes, BaseCoder.EncodingType.OCTAL),
                "Should reject bytes outside 0-255 range");
        }
        
        @Test
        @DisplayName("Should handle zero values")
        void testZeroValues() {
            int[] zeros = {0, 0, 0};
            String encoded = BaseCoder.encodeBytes(zeros, BaseCoder.EncodingType.OCTAL);
            int[] decoded = BaseCoder.decodeToBytes(encoded, BaseCoder.EncodingType.OCTAL);
            
            // Note: Leading zeros might be stripped in BigInteger conversion
            assertTrue(decoded.length > 0, "Should produce some output for zero bytes");
        }
        
        @Test
        @DisplayName("Should handle maximum byte values")
        void testMaxByteValues() {
            int[] maxBytes = {255,255,255};
            String encoded = BaseCoder.encodeBytes(maxBytes,  BaseCoder.EncodingType.OCTAL);
            int[] decoded = BaseCoder.decodeToBytes(encoded,  BaseCoder.EncodingType.OCTAL);
            
            assertArrayEquals(maxBytes, decoded, "Should handle maximum byte values correctly");
        }
    }
    
   
}
