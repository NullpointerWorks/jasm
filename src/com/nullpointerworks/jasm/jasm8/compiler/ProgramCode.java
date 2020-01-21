package com.nullpointerworks.jasm.jasm8.compiler;

public class ProgramCode 
{
	private int number = -1;
	private String line = "";
	
	public ProgramCode(int n, String l)
	{
		number = n;
		line = l;
	}
	
	public final int getLineNumber() {return number;}
	public final String getLineText() {return line;}
}
