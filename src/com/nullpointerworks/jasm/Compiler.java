package com.nullpointerworks.jasm;

import java.util.List;

import com.nullpointerworks.jasm.compiler.Draft;
import com.nullpointerworks.jasm.parser.SourceCode;

public interface Compiler<T>
{
	/**
	 * 
	 */
	Compiler<T> reset();
	
	/**
	 * 
	 */
	Compiler<T> setVerbose(boolean verbose);
	
	/**
	 * 
	 */
	Compiler<T> preprocess(Parser parser);
	
	/**
	 * 
	 */
	Draft<T> compile(int index, SourceCode loc);
	
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
	List<Draft<T>> getDraft();
}
