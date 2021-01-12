package com.nullpointerworks.jasm.assembler.errors;

import com.nullpointerworks.jasm.assembler.SourceCode;

public class PreProcessorError extends GenericError
{
	public PreProcessorError(SourceCode code, String desc) 
	{
		super(code, desc, "Pre-Processor Error");
	}
}
