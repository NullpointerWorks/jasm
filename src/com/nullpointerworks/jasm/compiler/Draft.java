package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.compiler.errors.BuildError;

public interface Draft<T>
{
	/**
	 * 
	 */
	SourceCode getLineOfCode();
	
	/**
	 * 
	 */
	T getInstruction();
	
	/**
	 * 
	 */
	boolean hasLabel();
	
	/**
	 * 
	 */
	String getLabel();
	
	/**
	 * 
	 */
	boolean hasError();
	
	/**
	 * 
	 */
	BuildError getError();
	
	/**
	 * 
	 */
	void setJumpAddress(int addr);
	
	/**
	 * 
	 */
	int getCodeIndex();
}
