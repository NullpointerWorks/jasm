package com.nullpointerworks.jasm.asm.error;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class TranslationError extends GenericError
{
	public TranslationError(SourceCode code, String desc) 
	{
		super(code, desc, "Translation Error");
	}
}
