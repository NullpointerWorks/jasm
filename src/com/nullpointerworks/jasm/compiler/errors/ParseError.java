package com.nullpointerworks.jasm.compiler.errors;

import com.nullpointerworks.jasm.compiler.SourceCode;

public class ParseError extends GenericError
{
	public ParseError(SourceCode code, String desc) 
	{
		super(code, desc, "Parser Error");
	}
}
