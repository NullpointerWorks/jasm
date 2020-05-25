package com.nullpointerworks.jasm.compiler;

public class ParseError extends GenericError
{
	public ParseError(SourceCode code, String desc) 
	{
		super(code, desc, "Parser Error");
	}
}
