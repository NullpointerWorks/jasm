package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.preprocessor.Preprocessor;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

/**
 * JASM 2 compiler interface
 */
public interface Compiler
{
	/**
	 * 
	 */
	Compiler reset();
	
	/**
	 * 
	 */
	Compiler setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	Compiler compile(Preprocessor preproc);
	
	/**
	 * 
	 */
	Compiler save(String filepath);
	
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
	List<Instruction> getInstructions();
}
