package com.nullpointerworks.jasm.compiler;

import java.util.List;

import com.nullpointerworks.jasm.compiler.errors.BuildError;

/**
 * JASM parser interface
 */
public interface Parser
{
	/**
	 * 
	 */
	void parse(String filename);
	
	/**
	 * 
	 */
	void setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	void setIncludesPath(List<String> path);
	
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
	
	/**
	 * 
	 */
	List<Definition> getDefinitions();
}
