package com.nullpointerworks.jasm.preprocessor;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.parser.SourceCode;

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
