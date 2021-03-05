package com.nullpointerworks.jasm2.asm.parser;

/**
 * A tuple class to represent a definition declared in a source file.
 * @author Michiel Drost - Nullpointer Works
 */
public final class Definition
{
	public final String NAME; 
	public final String VALUE;
	public final SourceCode SOURCE;
	
	public Definition(String a, String b, SourceCode c)
	{
		this.NAME=a;
		this.VALUE=b;
		this.SOURCE=c;
	}
}
