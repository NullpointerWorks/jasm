package com.nullpointerworks.jasm.parser;

/*
 * tuple delegate
 */
public class DefineRecord
{
	public final String NAME; 
	public final String VALUE;
	public final SourceCode SOURCE;
	
	public DefineRecord(String a, String b, SourceCode c)
	{
		this.NAME=a;
		this.VALUE=b;
		this.SOURCE=c;
	}
}
