package com.nullpointerworks.jasm.parser;

import java.util.List;

import com.nullpointerworks.jasm.BuildError;

/**
 * JASM parser interface
 */
public interface Parser
{
	/**
	 * 
	 */
	Parser reset();
	
	/**
	 * 
	 */
	Parser setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	Parser setIncludesPath(List<String> path);
	
	/**
	 * 
	 */
	Parser parse(String filename);
	
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
	List<SourceCode> getSourceCode();
}
