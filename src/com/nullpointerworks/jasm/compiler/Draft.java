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
	String getLabel();
	
	/**
	 * 
	 */
	BuildError getError();
	
	/**
	 * 
	 */
	int getCodeIndex();
	
	/**
	 * 
	 */
	T getInstruction();
	
	/**
	 * 
	 */
	void setJumpAddress(int addr);
	
	/**
	 * 
	 */
	boolean hasLabel();
	
	/**
	 * 
	 */
	boolean hasError();
}
