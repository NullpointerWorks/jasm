package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.compiler.errors.BuildError;

public interface Compiler
{
	/**
	 * 
	 */
	void compile(Parser parser);
	
	/**
	 * 
	 */
	List<Draft> getDraft();
	
	/**
	 * 
	 */
	void setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<BuildError> getErrors();
}
