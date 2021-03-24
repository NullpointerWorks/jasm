package com.nullpointerworks.jasm.asm.assembler;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Translation;

public interface Assembler 
{
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

	/**
	 * 
	 */
	void assemble(	List<Translation> translation, 
					List<Definition> definitions, 
					List<Allocation> allocations);
}
