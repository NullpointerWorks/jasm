package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;

public interface CodeTranslator 
{
	boolean hasErrors();
	BuildError getError();
	boolean hasOperation(String i);
	List<Translation> getTranslation(SourceCode sc);
}
