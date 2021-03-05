package com.nullpointerworks.jasm2.asm.error;

import com.nullpointerworks.jasm2.asm.parser.SourceCode;

public class ParseError extends GenericError
{
	public ParseError(SourceCode code, String desc) 
	{
		super(code, desc, "Parser Error");
	}
}
