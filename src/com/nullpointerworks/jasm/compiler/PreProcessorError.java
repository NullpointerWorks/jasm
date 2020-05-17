package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.BuildError;

public class PreProcessorError implements BuildError 
{
	private final SourceCode code;
	private final String desc;
	
	public PreProcessorError(SourceCode code, String desc)
	{
		this.code=code;
		this.desc=desc;
	}

	public SourceCode getSourceCode() {return code;}
	
	public String getDescription() {return desc;}

}
