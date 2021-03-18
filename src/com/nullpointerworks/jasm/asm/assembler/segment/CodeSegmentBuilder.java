package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.SuperDraftBuilder;
import com.nullpointerworks.jasm.asm.assembler.builder.DraftBuilder;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class CodeSegmentBuilder implements SegmentBuilder
{
	private Map<String, Integer> labels; // keeps track of instruction address and attached labels
	private List< Pair<Draft, Integer> > labelled; // 
	private DraftBuilder builder;
	
	private List<Integer> code; // byte code
	private VerboseListener verbose;
	private BuildError error;
	private int instIndex;
	
	public CodeSegmentBuilder()
	{
		labels = new HashMap<String, Integer>();
		labelled = new ArrayList< Pair<Draft, Integer> >();
		builder = new SuperDraftBuilder();
		
		code = new ArrayList<Integer>();
		error = null;
		instIndex = 0;
		verbose = (s)->{};
	}
	
	public void setVerboseListener(VerboseListener v) 
	{
		this.verbose=v;
	}
	
	public boolean hasError()
	{
		return error != null;
	}
	
	public BuildError getError()
	{
		return error;
	}
	
	public void addSourceCode(SourceCode sc) 
	{
		String line = sc.getLine();
		
		/*
		 * is a label, store for later reference along with it's instruction address
		 */
		if (line.contains(":"))
		{
			String label = line.substring(0,line.length()-1);
			labels.put(label.toLowerCase(), instIndex); // labels are not case sensitive
			return;
		}
		
		/*
		 * build a draft from the source code
		 */
		List<Draft> draft_inst = builder.buildDraft(sc);
		if (builder.hasError())
		{
			setError( builder.getError() );
			return;
		}
		
		/*
		 * check all drafts, add it's machine code
		 */
		for (Draft d : draft_inst)
		{
			if (d.hasError())
			{
				setError( d.getError() );
				return;
			}
			
			List<Integer> c = d.getMachineCode();
			for (Integer i : c) code.add(i);
			
			// label insertion is only done with jump instructions
			if (d.hasLabel())
			{
				var p = new Pair<Draft, Integer>(d, instIndex + 1);
				labelled.add(p);
			}
			
			instIndex += c.size();
		}
	}
	
	public void finish(int offset)
	{
		
		
		
		
		
		
	}
	
	private void insertLabels(List< Pair<Draft, Integer> > labelled, 
							  Map<String, Integer> labels,
							  List<Integer> code) 
	{
		verbose.onPrint("Labels");
		for (Pair<Draft, Integer> p : labelled)
		{
			Draft d = p.First;
			int index = p.Second;
			
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				setError(d.getSourceCode(), "  Unknown label reference; "+label);
				return;
			}
			
			int addr = labels.get(label);
			code.set(index, addr);
			verbose.onPrint("  "+label+": 0x"+String.format("%x", addr) );
		}
	}
	
	private void setError(BuildError err) 
	{
		error = err;
	}
	
	private void setError(SourceCode sc, String message)
	{
		error = new AssembleError(sc,message);
	}
}
