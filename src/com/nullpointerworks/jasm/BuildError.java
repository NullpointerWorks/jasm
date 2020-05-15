package com.nullpointerworks.jasm;

import com.nullpointerworks.jasm.parser.SourceCode;

public interface BuildError 
{
	SourceCode getSourceCode();
	String getDescription();
}
