package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.ctrlflow.*;
import com.nullpointerworks.jasm.asm.assembler.builder.dataflow.*;
import com.nullpointerworks.jasm.asm.assembler.builder.sys.*;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SuperDrafter implements Drafter
{
	private BuildError error;
	private List<Drafter> drafters;
	
	public SuperDrafter()
	{
		drafters = new ArrayList<Drafter>();
		drafters.add( new NopDrafter() );
		drafters.add( new IntDrafter() );
		
		drafters.add( new JumpDrafter() );
		
		drafters.add( new LoadDrafter() );
		
		
		
		
	}

	@Override
	public boolean hasError() 
	{
		return error != null;
	}

	@Override
	public BuildError getError() 
	{
		return error;
	}
	
	@Override
	public boolean isInstruction(ASMInstruction instruct) 
	{
		for (Drafter dr : drafters)
		{
			if (dr.isInstruction(instruct)) return true;
		}
		return false;
	}
	
	@Override
	public Draft draft(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs, 
						List<Label> lbls) 
	{
		error = null;
		ASMInstruction inst = tr.getInstruction();
		
		if (!isInstruction(inst))
		{
			error = new AssembleError(tr.getSourceCode(), "  Failed to find a drafter for this instruction.");
			return null;
		}
		
		for (Drafter dr : drafters)
		{
			if (dr.isInstruction(inst))
			{
				Draft d = dr.draft(tr, defs, allocs, lbls);
				if (dr.hasError())
				{
					error = dr.getError();
				}
				return d;
			}
		}
		error = new AssembleError(tr.getSourceCode(), "  Internal error trying to draft translation.");
		return null;
	}
}
