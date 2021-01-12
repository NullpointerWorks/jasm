package com.nullpointerworks.jasm.assembler;

import java.util.List;

import com.nullpointerworks.jasm.assembler.errors.BuildError;

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
