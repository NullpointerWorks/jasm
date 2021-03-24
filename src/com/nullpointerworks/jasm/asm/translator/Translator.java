package com.nullpointerworks.jasm.asm.translator;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public interface Translator 
{
	
	/**
	 * 
	 */
	void setVerboseListener(VerboseListener v);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<BuildError> getErrors();
	
	/**
	 * 
	 */
	List<Definition> getDefinitions();
	
	/**
	 * 
	 */
	List<Translation> getTranslation();
	
	/**
	 * 
	 */
	void translate(List<SourceCode> sourcecode);
}
