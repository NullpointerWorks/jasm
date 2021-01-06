package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.compiler.errors.BuildError;

public interface Compiler<T>
{
	/**
	 * 
	 */
	void setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	void preprocess(Parser parser);
	
	/**
	 * 
	 */
	List<T> getInstructions();
	
	/**
	 * 
	 */
	Draft<T> compile(int index, SourceCode loc);
	
	/**
	 * 
	 */
	Draft<T> draft(SourceCode loc, Operation op, String operands);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<BuildError> getErrors();
}
