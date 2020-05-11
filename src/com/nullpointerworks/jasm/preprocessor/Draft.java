package com.nullpointerworks.jasm.preprocessor;

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
	void setLabelAddress(int addr);
	
	/**
	 * 
	 */
	int getCodeIndex();
	
	/**
	 * 
	 */
	DraftError getDraftError();
}
