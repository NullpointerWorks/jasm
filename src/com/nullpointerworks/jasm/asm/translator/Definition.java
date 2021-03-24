package com.nullpointerworks.jasm.asm.translator;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class Definition 
{
	private SourceCode source;
	private Directive type = Directive.NULL;
	
	private String name;
	private Number number;
	
	public Definition(Directive t, SourceCode sc, String n, int v) 
	{
		type = t;
		source = sc;
		name = n;
		number = new Number(v);
	}
	
	public SourceCode getSourceCode()
	{
		return source;
	}
	
	public Directive getDirective()
	{
		return type;
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
