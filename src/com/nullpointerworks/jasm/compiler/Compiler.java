package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.compiler.errors.BuildError;

public interface Compiler
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
	void compile();
	
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
