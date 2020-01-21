package com.nullpointerworks.jasm.jasm8.compiler;

public class SourceCode 
{
	private int number = -1;
	private String line = "";
	private String file = "";
	
	public SourceCode(int n, String l, String f)
	{
		number = n;
		line = l;
		file = f;
	}
	
	public final int getLineNumber() {return number;}
	public final String getLineText() {return line;}
	public final String getSourceFile() {return file;}
}
