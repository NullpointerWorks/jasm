package com.nullpointerworks.jasm.asm.error;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class AssembleError extends GenericError
{
	public AssembleError(SourceCode code, String desc) 
	{
		super(code, desc, "Assembler Error");
	}
}
