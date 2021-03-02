package com.nullpointerworks.jasm.assembly.errors;

import com.nullpointerworks.jasm.assembly.parser.SourceCode;

public class ParseError extends GenericError
{
	public ParseError(SourceCode code, String desc) 
	{
		super(code, desc, "Parser Error");
	}
}
