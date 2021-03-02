package com.nullpointerworks.jasm.assembly.errors;

import com.nullpointerworks.jasm.assembly.parser.SourceCode;

public class AssemblerError extends GenericError
{
	public AssemblerError(SourceCode code, String desc) 
	{
		super(code, desc, "Assembler Error");
	}
}
