package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.HashMap;
import java.util.Map;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;

/*
 * the label manager couples labels and addresses. Which value 
 * is contained in the Number objects is managed by the segment 
 * builders.
 */
public class LabelManager 
{
	private Map<String, Number> labels; // keeps track of instruction address and attached labels
	private VerboseListener verbose;
	private BuildError error;
	
	public LabelManager()
	{
		labels = new HashMap<String, Number>();
		error = null;
		verbose = (s)->{};
	}
	
	public void setVerboseListener(VerboseListener v) 
	{
		this.verbose = v;
	}
	
	public void addLabelPointer(String label, Number index) 
	{
		labels.put(label, index);
	}
	
	public void addDefinitionPointer(String label, Number index) 
	{
		labels.put(label, index);
	}
	
	public boolean hasError()
	{
		return error != null;
	}
	
	public BuildError getError()
	{
		return error;
	}
}
