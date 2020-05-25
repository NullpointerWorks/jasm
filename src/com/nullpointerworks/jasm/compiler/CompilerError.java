package com.nullpointerworks.jasm.compiler;

public class CompilerError extends GenericError
{
	public CompilerError(SourceCode code, String desc) 
	{
		super(code, desc, "Compiler Error");
	}
}
