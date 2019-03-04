package com.dm.library;
import java.math.BigInteger;

public class DecriptText {
	private static BigInteger ClientprivateKey=new BigInteger("21719");
	private static BigInteger ClientpublicKey=new BigInteger("59");
	private static BigInteger Clientmodulus= new BigInteger("37001");
	String encryptText;
	public static String EncryptData(String MyMessage)
	{
	
		String EncryptedMessage = "";
    
		BigInteger mg;
	    BigInteger encrypt;
	    for(int i=0;i< MyMessage.length();i++)
                     {

                                if(i>0)
	    	  EncryptedMessage+=" ";
	    	  mg = intToBigint((int) MyMessage.charAt(i));
	    	  encrypt = encrypt(mg,ClientprivateKey,Clientmodulus);
	    	  EncryptedMessage+= encrypt.toString();
	      }
	      
	      return EncryptedMessage;
  }
  

                                       

                                   
  public static String  DecryptData(String MyMessage)
  {
	  String DecryptedMessage = "";
	  BigInteger mg;
	  BigInteger decrypt;
	  String[] tokens = MyMessage.split(" ");
	  int ascii;
	  char ch;
	  for(int i=0;i< tokens.length; i++)
	  {
	   	  mg = intToBigint(Integer.parseInt(tokens[i]));
	   	  decrypt = decrypt(mg,ClientpublicKey,Clientmodulus);
	   	  ascii = decrypt.intValue();
	   	  ch = (char) ascii;
	   	  DecryptedMessage+=  ch;
	   	  
	  }

	  return DecryptedMessage;
}
  
static BigInteger encrypt(BigInteger message,BigInteger privatekey,BigInteger modulus) {
		      return message.modPow(privatekey, modulus);
}

static BigInteger decrypt(BigInteger encrypted,BigInteger publickey,BigInteger modulus) {
		      return encrypted.modPow(publickey, modulus);
}
  
	  static BigInteger intToBigint(int x) {  
	       return BigInteger.valueOf(x);  
	    }	
}
