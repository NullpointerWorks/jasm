package com.nullpointerworks.jasm;

import com.nullpointerworks.jasm.compiler.SourceCode;

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
