package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

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
