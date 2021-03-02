package com.nullpointerworks.jasm.assembly;

import java.util.List;

import com.nullpointerworks.jasm.assembly.drafting.Draft;
import com.nullpointerworks.jasm.assembly.errors.BuildError;
import com.nullpointerworks.jasm.assembly.parser.Definition;
import com.nullpointerworks.jasm.assembly.parser.SourceCode;

public interface Assembler
{
	/**
	 * 
	 */
	void setSourceCode(List<SourceCode> sc);
	
	/**
	 * 
	 */
	void setDefinitions(List<Definition> df);
	
	/**
	 * 
	 */
	void setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	void assemble();
	
	/**
	 * 
	 */
	List<Draft> getDraft();
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<BuildError> getErrors();
}
