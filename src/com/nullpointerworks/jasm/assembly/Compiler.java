package com.nullpointerworks.jasm.assembly;

import java.util.List;

import com.nullpointerworks.jasm.assembly.drafting.Draft;

public interface Compiler<T>
{
	/**
	 * 
	 * @return
	 */
	void setDraft(List<Draft> draft);
	
	/**
	 * 
	 * @return
	 */
	List<T> getInstructions();

	/**
	 * 
	 * @return
	 */
	void build();
}
