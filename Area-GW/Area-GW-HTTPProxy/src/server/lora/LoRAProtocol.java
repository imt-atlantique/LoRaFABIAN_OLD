package server.lora;

public class LoRAProtocol {
	
	/* -------------------------------------------------------------------------- */
	/* --- PRIVATE CONSTANTS ---------------------------------------------------- */

	public final static byte PROTOCOL_VERSION	= (byte)0x01;
	
	public final static byte PKT_PUSH_DATA 		= (byte)0x00 ;
	public final static byte PKT_PUSH_ACK		= (byte)0x01 ;
	public final static byte PKT_PULL_DATA		= (byte)0x02 ;
	public final static byte PKT_PULL_RESP		= (byte)0x03 ;
	public final static byte PKT_PULL_ACK		= (byte)0x04 ;
	
	public final static int PKT_ACK_SIZE = 4;
	
	

	/*
	 * 
	 * Code from:
	 * 		* http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
	 *  
	 * 		* This can be replaced with:
	 * 			 org.apache.commons.codec.binary.Hex.encodeHexString(byte[] bytes); library that I already include
	 * 		* This is what Ive done. I no longer use this function.
	 * 
	 * */
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
