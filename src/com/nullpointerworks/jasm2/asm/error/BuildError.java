package com.nullpointerworks.jasm2.asm.error;

import com.nullpointerworks.jasm2.asm.parser.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
