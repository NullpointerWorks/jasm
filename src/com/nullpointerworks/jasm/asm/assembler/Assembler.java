package com.nullpointerworks.jasm.asm.assembler;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Definition;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public interface Assembler 
{
	/**
	 * 
	 */
	void draft(List<SourceCode> sourcecode, List<Definition> definitions, int origin);
	
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
