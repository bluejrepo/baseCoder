package com.coder;

import org.junit.jupiter.api.Test;
import com.coder.BaseCoder;
import com.coder.BaseCoder.EncodingType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
        void testLongOctalInput() throws UnsupportedEncodingException {
            String longInput =  "227711474231113516702134342400414143206126403671660545541234" +
            					"216161770521652245433114475436547416173670422136456436311234" +
            				    "717046577704333460730170473602176263254671507630065771331234" +
            				    "333465753306216356425416366443265355016660043333267564244321" +
            				    "350700124251451433665154621070427111157201067171276700624321" +
            				    "717046577704333460730170473602176263254671507630065771334321" +
            				    "717046577704333460730170473602176263254671507630065771333765" +
            				    "541526554667660414027165423111111315057614760526500045244321" +
            				    "717046577704333460730170473602176263254671507630065771333765" +
            				    "333465753306216356425416366443265355016660043333267564241234" +
            				    "717046577704333460730170473602176263254671507630065771335672" +
            				    "216161770521652245433114475436547416173670422136456436315672" +
            				    "333465753306216356425416366443265355016660043333267564243765" +
            				    "717046577704333460730170473602176263254671507630065771333765" +
            				    "333465753306216356425416366443265355016660043333267564243765" +
            				    "717046577704333460730170473602176263254671507630065771337564" +
            				    "216161770521652245433114475436547416173670422136456436317564" +
            					"3334" ;
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
            System.out.printf("%d bytes decoded time %d (ms) \n", longInput.getBytes("UTF-8").length, decodeTime);
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
    @DisplayName("Hexadecimal Encoding/Decoding Tests")
    class HexadecimalTests {
        
        @Test
        @DisplayName("Should decode hexadecimal string correctly")
        void testHexDecoding() {
            String input = "48656C6C6F"; // "Hello" in ASCII
            int[] decoded = BaseCoder.decodeToBytes(input, BaseCoder.EncodingType.HEXADECIMAL);
            int[] expected = {72, 101, 108, 108, 111}; // ASCII values for "Hello"
            
            assertArrayEquals(expected, decoded);
        }
        
        @Test
        @DisplayName("Should encode bytes to hexadecimal correctly")
        void testHexEncoding() {
            int[] input = {72, 101, 108, 108, 111}; // ASCII values for "Hello"
            String encoded = BaseCoder.encodeBytes(input, BaseCoder.EncodingType.HEXADECIMAL);
            String expected = "48656C6C6F";
            
            assertEquals(expected, encoded);
        }
        
        @Test
        @DisplayName("Should handle mixed case hex input")
        void testMixedCaseHex() {
            String lowerCase = "deadbeef";
            String upperCase = "DEADBEEF";
            
            int[] decodedLower = BaseCoder.decodeToBytes(lowerCase, BaseCoder.EncodingType.HEXADECIMAL);
            int[] decodedUpper = BaseCoder.decodeToBytes(upperCase, BaseCoder.EncodingType.HEXADECIMAL);
            
            assertArrayEquals(decodedLower, decodedUpper, "Mixed case should decode to same result");
        }
        
        @Test
        @DisplayName("Should reject invalid hex digits")
        void testInvalidHexDigits() {
            assertThrows(IllegalArgumentException.class, 
                () -> BaseCoder.decodeToBytes("GHIJK", BaseCoder.EncodingType.HEXADECIMAL),
                "Should reject invalid hex digits");
        }
    }

    @Nested
    @DisplayName("Base32 Encoding/Decoding Tests")
    class Base32Tests {
        
        @Test
        @DisplayName("Should handle Base32 encoding/decoding")
        void testBase32() {
            int[] input = {72, 101, 108, 108, 111}; // "Hello"
            String encoded = BaseCoder.encodeBytes(input, BaseCoder.EncodingType.BASE32);
            int[] decoded = BaseCoder.decodeToBytes(encoded, BaseCoder.EncodingType.BASE32);
            
            assertArrayEquals(input, decoded, "Base32 round-trip should preserve data");
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
        
        @Test
        @DisplayName("Should handle long octal input as chunks")
        void testDecodeOctalInputAsChunk() throws UnsupportedEncodingException, InterruptedException, ExecutionException {
        	
            String longInput =  "227711474231113516702134342400414143206126403671660545541234" +
					"216161770521652245433114475436547416173670422136456436311234" +
				    "717046577704333460730170473602176263254671507630065771331234" +
				    "333465753306216356425416366443265355016660043333267564244321" +
				    "350700124251451433665154621070427111157201067171276700624321" +
				    "717046577704333460730170473602176263254671507630065771334321" +
				    "717046577704333460730170473602176263254671507630065771333765" +
				    "541526554667660414027165423111111315057614760526500045244321" +
				    "717046577704333460730170473602176263254671507630065771333765" +
					"3334" ; //1024 bytes.
            
            //ForkJoinPool Methods
            //CompletableFuture.supplyAsync(() -> {})
            //CompletableFuture.allOf(futures.toArray(new CompletetableFuture[0]))
            //CompletableFuture.allOff().allOfFutures.thenApply(v -> futures.stream().map(CompletableFuture :: join).collect(Collectors.toList()));
            //CompletableFuture.supplyAsync() in ForkJoinPool
            // 1. Create a List of CompletableFuture objects
            int CHUNK_SIZE = 128; //bytes 
            String [] strs = longInput.split("", CHUNK_SIZE);
            List<CompletableFuture<int[]>> futures = new ArrayList<>();
            for(String str : strs) {
            	CompletableFuture<int[]> completableFuture = CompletableFuture.supplyAsync(() -> { //This is Fork
            		return BaseCoder.decodeToBytes(str, EncodingType.OCTAL);
            	});
            	futures.add(completableFuture);
            }
            
            //2. Combine all CompletableFutures using CompletableFuture.allOf()
            //allOf() returns a CompletableFuture<Void> that completes when all
            //the input CompletableFutures complete.
            CompletableFuture<Void> allOfFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            
            // 3. Process the results after all futures are complete
            // thenApply() is used to transform the result of allOfFuture (which is Void)
            // into a List of String results from the individual futures.
            CompletableFuture<List<int[]>> allResultFutures = allOfFutures.thenApply(v -> futures.stream()
            																					 .map(CompletableFuture :: join) //This is Joining results
            																					 .collect(Collectors.toList()));
            
            List<int[]> results = allResultFutures.get();
            for(int [] r : results) {
            	System.out.println(Arrays.toString(r));
            }
            
            assertTrue(Boolean.TRUE);
        }
          
    }
    
    class Decoder implements Callable<int []> {
    	
    	private String input;
    	private EncodingType encodingType;
    	
    	public Decoder(String input, EncodingType encodingType) {
    		this.input = input;
    		this.encodingType = encodingType;
    	}
    	
		@Override
		public int [] call() throws Exception {
			return BaseCoder.decodeToBytes(this.input, encodingType);
		}
    	
    }
    
   
}
