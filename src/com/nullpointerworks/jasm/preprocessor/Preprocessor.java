package com.nullpointerworks.jasm.preprocessor;

import java.util.List;

import com.nullpointerworks.jasm.BuildError;
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
	List<BuildError> getErrors();
	
	/**
	 * 
	 */
	List<DraftJASM> getDraft();
}
