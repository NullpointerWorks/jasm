package com.nullpointerworks.jasm2.asm.parser;

import java.util.List;

import com.nullpointerworks.jasm2.asm.VerboseListener;
import com.nullpointerworks.jasm2.asm.error.BuildError;

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
	void setVerboseListener(VerboseListener verbose);
	
	/**
	 * 
	 */
	void addIncludesPath(String path);
	
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
