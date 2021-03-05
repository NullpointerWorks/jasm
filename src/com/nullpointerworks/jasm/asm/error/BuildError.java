package com.nullpointerworks.jasm.asm.error;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
