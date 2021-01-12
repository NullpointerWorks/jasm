package com.nullpointerworks.jasm.assembler.errors;

import com.nullpointerworks.jasm.assembler.SourceCode;

public class ParseError extends GenericError
{
	public ParseError(SourceCode code, String desc) 
	{
		super(code, desc, "Parser Error");
	}
}
