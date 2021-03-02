package com.nullpointerworks.jasm.assembly.errors;

import com.nullpointerworks.jasm.assembly.parser.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
