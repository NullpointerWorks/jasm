package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.SuperDraftBuilder;
import com.nullpointerworks.jasm.asm.assembler.builder.DraftBuilder;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class CodeSegmentBuilder implements SegmentBuilder
{
	private LabelManager manager;
	private DraftBuilder builder;
	private List<Number> code; // byte code
	private VerboseListener verbose;
	private BuildError error;
	private int instIndex;
	
	public CodeSegmentBuilder(LabelManager m)
	{
		manager = m;
		builder = new SuperDraftBuilder();
		code = new ArrayList<Number>();
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
			manager.addLabelPointer(label.toLowerCase(), instIndex); // labels are not case sensitive
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
			for (Integer i : c) 
			{
				code.add( new Number(i) );
			}
			
			// label insertion is only done with jump instructions
			if (d.hasLabel())
			{
				manager.addLabelledDraft(d);
			}
			
			instIndex += c.size();
		}
	}
	
	public void finish(int offset)
	{
		
		
		
		
		
		
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
