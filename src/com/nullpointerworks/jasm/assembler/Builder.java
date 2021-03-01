package com.nullpointerworks.jasm.assembler;

import java.util.List;

public interface Builder<T>
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
