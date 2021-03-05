package com.nullpointerworks.jasm2.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm2.asm.assembler.Draft;
import com.nullpointerworks.jasm2.asm.error.BuildError;
import com.nullpointerworks.jasm2.asm.parser.SourceCode;

/**
 * 
 */
public interface IDraftBuilder 
{
	/**
	 * 
	 * @return
	 */
	boolean hasOperation(String instruct);
	
	/**
	 * 
	 * @return
	 */
	List<Draft> buildDraft(SourceCode sc);
	
	/**
	 * 
	 * @return
	 */
	boolean hasError();
	
	/**
	 * 
	 * @return
	 */
	void setError(BuildError err);
	
	/**
	 * 
	 * @return
	 */
	BuildError getError();
}
