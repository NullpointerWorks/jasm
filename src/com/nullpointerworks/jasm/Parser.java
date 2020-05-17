package com.nullpointerworks.jasm;

import java.util.List;

import com.nullpointerworks.jasm.compiler.DefineRecord;
import com.nullpointerworks.jasm.compiler.SourceCode;

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
	
	/**
	 * 
	 */
	List<DefineRecord> getDefinitions();
}
