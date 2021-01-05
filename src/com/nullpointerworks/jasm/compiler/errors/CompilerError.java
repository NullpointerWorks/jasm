package com.nullpointerworks.jasm.compiler.errors;

import com.nullpointerworks.jasm.compiler.SourceCode;

public class CompilerError extends GenericError
{
	public CompilerError(SourceCode code, String desc) 
	{
		super(code, desc, "Compiler Error");
	}
}
