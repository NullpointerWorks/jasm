package com.nullpointerworks.jasm.asm.translator;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class Label 
{
	private SourceCode source;
	private String name;
	private Number number;
	
	public Label(SourceCode sc, String n) 
	{
		source = sc;
		name = n;
		number = new Number(0);
	}
	
	public SourceCode getSourceCode()
	{
		return source;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Number getNumber()
	{
		return number;
	}
}
