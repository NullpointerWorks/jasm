package com.nullpointerworks.jasm;

import com.nullpointerworks.jasm.compiler.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
