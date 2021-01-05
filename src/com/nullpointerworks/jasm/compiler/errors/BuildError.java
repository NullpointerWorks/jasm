package com.nullpointerworks.jasm.compiler.errors;

import com.nullpointerworks.jasm.compiler.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
