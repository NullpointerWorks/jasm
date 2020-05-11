package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.preprocessor.Preprocessor;

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
	Compiler compile(Preprocessor parser);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<CompileError> getErrors();
	
	/**
	 * 
	 */
	List<Instruction> getInstructions();
}
