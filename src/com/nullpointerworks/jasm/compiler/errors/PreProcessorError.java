package com.nullpointerworks.jasm.compiler.errors;

import com.nullpointerworks.jasm.compiler.SourceCode;

public class PreProcessorError extends GenericError
{
	public PreProcessorError(SourceCode code, String desc) 
	{
		super(code, desc, "Pre-Processor Error");
	}
}
