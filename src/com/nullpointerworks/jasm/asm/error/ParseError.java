package com.nullpointerworks.jasm.asm.error;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class ParseError extends GenericError
{
	public ParseError(SourceCode code, String desc) 
	{
		super(code, desc, "Parser Error");
	}
}
