package com.nullpointerworks.jasm.assembler.errors;

import com.nullpointerworks.jasm.assembler.SourceCode;

public class AssemblerError extends GenericError
{
	public AssemblerError(SourceCode code, String desc) 
	{
		super(code, desc, "Assembler Error");
	}
}
