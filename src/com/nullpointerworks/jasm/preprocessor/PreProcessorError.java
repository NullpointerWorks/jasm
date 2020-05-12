package com.nullpointerworks.jasm.preprocessor;

import com.nullpointerworks.jasm.parser.SourceCode;

public class PreProcessorError 
{
	private final SourceCode code;
	private final String desc;
	
	public PreProcessorError(SourceCode code, String desc)
	{
		this.code=code;
		this.desc=desc;
	}
	
	public String getDescription() {return desc;}

}
