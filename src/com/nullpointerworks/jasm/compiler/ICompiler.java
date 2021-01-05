package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.compiler.errors.BuildError;

public interface ICompiler<T>
{
	/**
	 * 
	 */
	void setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	void preprocess(IParser parser);
	
	/**
	 * 
	 */
	List<Draft<T>> getDraft();
	
	/**
	 * 
	 */
	Draft<T> compile(int index, SourceCode loc);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<BuildError> getErrors();
}
