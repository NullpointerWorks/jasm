package com.nullpointerworks.jasm.asm;

public class ParserUtility 
{
	public static final String ADDRESS_MARK 	= "&";
	public static final String LABEL_MARK 		= ":";
	
	private static final String INTEGER 		= "[+-]?\\d+";
	private static final String HEXADEC 		= "^(0x|0X|#).[0-9A-Fa-f]+";
	
	public static boolean isInteger(String str)
	{
		return str.matches(INTEGER);
	}
	
	public static boolean isHexadec(String str)
	{
		return str.matches(HEXADEC);
	}
	
	public static boolean isValidFile(String fn) 
	{
		if (fn.endsWith(".jasm") ) return true;
		if (fn.endsWith(".asm") ) return true;
		return false;
	}
	
	public static String fillFromBack(String msg, String chr, int leng) 
	{
		String filler = "";
		for (int s=0; s<leng; s++) filler += chr;
		String concat = filler+msg;
		int strLeng = concat.length();
		return concat.substring(strLeng-leng, strLeng);
	}
	
	public static boolean isValidLabel(String label)
	{
		if ( isValidNumber(label) ) return false;
		if ( label.matches("\\D[a-zA-Z0-9\\_]+") ) return true;
		return false;
	}
	
	public static boolean isAddress(String str)
	{
		return str.startsWith(ADDRESS_MARK);
	}
	
	public static boolean isValidAddress(String number)
	{
		if (!isAddress(ADDRESS_MARK)) return false;
		number = number.substring(1);
		return isValidNumber(number);
	}
	
	public static boolean isValidNumber(String number)
	{
		if (isInteger(number)) return true;
		if (isHexadec(number)) return true;
		return false;
	}
}
