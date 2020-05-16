package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.parser.SourceCode;

public class CompilerError implements BuildError 
{
	private final SourceCode code;
	private final String desc;
	
	public CompilerError(SourceCode code, String desc)
	{
		this.code=code;
		this.desc=desc;
	}

	public SourceCode getSourceCode() {return code;}
	
	public String getDescription() {return desc;}

}
