package com.nullpointerworks.jasm2.asm.error;

import com.nullpointerworks.jasm2.asm.parser.SourceCode;

public class AssembleError extends GenericError
{
	public AssembleError(SourceCode code, String desc) 
	{
		super(code, desc, "Assembler Error");
	}
}
