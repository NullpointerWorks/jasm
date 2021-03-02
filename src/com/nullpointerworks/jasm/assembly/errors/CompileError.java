package com.nullpointerworks.jasm.assembly.errors;

import com.nullpointerworks.jasm.assembly.parser.SourceCode;

public class CompileError extends GenericError
{
	public CompileError(SourceCode code, String desc) 
	{
		super(code, desc, "Pre-Processor Error");
	}
}
