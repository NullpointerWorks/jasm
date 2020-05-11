package com.nullpointerworks.jasm.preprocessor;

import java.util.List;

import com.nullpointerworks.jasm.compiler.Instruction;
import com.nullpointerworks.jasm.parser.Parser;

public interface Preprocessor 
{
	/**
	 * 
	 */
	Preprocessor reset();
	
	/**
	 * 
	 */
	Preprocessor setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	Preprocessor preprocess(Parser parser);
	
	/**
	 * 
	 */
	boolean hasErrors();
	
	/**
	 * 
	 */
	List<PreprocessError> getErrors();
	
	/**
	 * 
	 */
	List<Instruction> getInstructions();
}
