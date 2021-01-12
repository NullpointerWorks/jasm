package com.nullpointerworks.jasm.assembler.errors;

import com.nullpointerworks.jasm.assembler.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
