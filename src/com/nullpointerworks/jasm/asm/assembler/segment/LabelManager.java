package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

/*
 * the label manager couples labels and addresses. Which value 
 * is contained in the Number objects is managed by the segment 
 * builders.
 */
public class LabelManager 
{
	private Map<String, Number> labels; // keeps track of instruction address and attached labels
	private List< Pair<Draft, Number> > labelled; // stores all drafts that refer to a label
	private VerboseListener verbose;
	private BuildError error;
	
	public LabelManager()
	{
		labels = new HashMap<String, Number>();
		labelled = new ArrayList< Pair<Draft, Number> >();
		error = null;
		verbose = (s)->{};
	}
	
	public void setVerboseListener(VerboseListener v) 
	{
		this.verbose = v;
	}
	
	public void setCommitLabels() 
	{
		insertLabels(labelled, labels);
	}
	
	private void setError(SourceCode sc, String message)
	{
		error = new AssembleError(sc,message);
	}
	
	public void addLabelPointer(String draft, Number index) 
	{
		labels.put(draft, index);
	}
	
	public void addLabelCandidate(Draft l, Number n) 
	{
		labelled.add( new Pair<Draft, Number>(l, n) );
	}
	
	public boolean hasError()
	{
		return error != null;
	}
	
	public BuildError getError()
	{
		return error;
	}
	
	private void insertLabels(List< Pair<Draft, Number> > labelled, Map<String, Number> labels) 
	{
		verbose.onPrint("Labels");
		for (Pair<Draft, Number> p : labelled)
		{
			Draft draft = p.First;
			Number index = p.Second;
			
			String label = draft.getLabel();
			if (!labels.containsKey(label))
			{
				setError(draft.getSourceCode(), "  Unknown label reference; "+label);
				return;
			}
			
			int addr = labels.get(label).getValue();
			index.setValue(addr);
			
			verbose.onPrint("  "+label+": 0x"+String.format("%x", addr) );
		}
	}
}
