package com.nullpointerworks.jasm.asm.parser;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;

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
	
	/**
	 * 
	 */
	List<Definition> getAllocations();
	
	/**
	 * 
	 */
	int getOrigin();
}
