package com.nullpointerworks.jasm.parser;

import java.util.List;

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
	List<ParseError> getErrors();
	
	/**
	 * 
	 */
	List<SourceCode> getSourceCode();
	
	/**
	 * 
	 */
	List<EquRecord> getDefinitions();
}
