package com.nullpointerworks.jasm.jasm8.compiler;

public class CodeLine 
{
	private int number = -1;
	private String line = "";
	
	public CodeLine(int n, String l)
	{
		number = n;
		line = l;
	}
	
	public final int getLineNumber() {return number;}
	public final String getLineCode() {return line;}
}
