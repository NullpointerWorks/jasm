package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Instruction;
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
	public boolean isInstruction(Instruction instruct) 
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
		Instruction inst = tr.getInstruction();
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
