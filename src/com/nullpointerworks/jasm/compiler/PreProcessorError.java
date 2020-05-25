package com.nullpointerworks.jasm.compiler;

public class PreProcessorError extends GenericError
{
	public PreProcessorError(SourceCode code, String desc) 
	{
		super(code, desc, "Pre-Processor Error");
	}
}
