package com.nullpointerworks.jasm2.asm.parser;

class ParseUtil 
{
	private static final String INTEGER 	= "[+-]?\\d+";
	private static final String HEXADEC 	= "^(0x|0X|#).[0-9A-Fa-f]+";
	
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
}
