package com.nullpointerworks.jasm;

import java.util.List;

import com.nullpointerworks.jasm.parser.SourceCode;

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
