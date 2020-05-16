package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.parser.SourceCode;

public interface Draft<Type>
{
	/**
	 * 
	 */
	SourceCode getLineOfCode();
	
	/**
	 * 
	 */
	Type getInstruction();
	
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
