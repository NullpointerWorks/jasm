package com.nullpointerworks.jasm;

import java.util.List;

import com.nullpointerworks.jasm.preprocessor.DraftJASM;

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
