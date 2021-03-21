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

public class LabelManager 
{
	private Map<String, Integer> labels; // keeps track of instruction address and attached labels
	private List< Pair<Draft, Number> > labelled; // stores all drafts that refer to a label
	private VerboseListener verbose;
	private BuildError error;
	
	
	public LabelManager()
	{
		labels = new HashMap<String, Integer>();
		labelled = new ArrayList< Pair<Draft, Number> >();
		error = null;
		verbose = (s)->{};
	}
	
	public void setVerboseListener(VerboseListener v) 
	{
		this.verbose = v;
	}
	
	public void addLabelPointer(String draft, int index) 
	{
		labels.put(draft, index);
	}
	
	public void addLabelCandidate(Draft l, Number n) 
	{
		labelled.add( new Pair<Draft, Number>(l, n) );
	}

	public void setLabelAddress() 
	{
		insertLabels(labelled, labels);
	}
	
	private void insertLabels(List< Pair<Draft, Number> > labelled, 
							  Map<String, Integer> labels) 
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
			
			int addr = labels.get(label);
			index.setValue(addr);
			
			verbose.onPrint("  "+label+": 0x"+String.format("%x", addr) );
		}
	}
	
	void setError(SourceCode sc, String message)
	{
		error = new AssembleError(sc,message);
	}
}
