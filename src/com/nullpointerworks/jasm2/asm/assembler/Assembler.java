package com.nullpointerworks.jasm2.asm.assembler;

import java.util.List;

import com.nullpointerworks.jasm2.asm.VerboseListener;
import com.nullpointerworks.jasm2.asm.error.BuildError;
import com.nullpointerworks.jasm2.asm.parser.Definition;
import com.nullpointerworks.jasm2.asm.parser.SourceCode;

public interface Assembler 
{
	/**
	 * 
	 */
	void draft(List<SourceCode> sourcecode, List<Definition> definitions);
	
	/**
	 * 
	 */
	void setVerboseListener(VerboseListener verbose);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<BuildError> getErrors();
	
	/**
	 * 
	 */
	List<Integer> getMachineCode();
}
