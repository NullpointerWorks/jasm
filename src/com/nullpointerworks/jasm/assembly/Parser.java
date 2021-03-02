package com.nullpointerworks.jasm.assembly;

import java.util.List;

import com.nullpointerworks.jasm.assembly.errors.BuildError;
import com.nullpointerworks.jasm.assembly.parser.Definition;
import com.nullpointerworks.jasm.assembly.parser.SourceCode;

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
