package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SuperDraftAssembler implements DraftAssembler
{
	private BuildError error;
	private DraftAssembler sysDrafter;
	
	public SuperDraftAssembler()
	{
		sysDrafter = new SystemAssembler();
		error = null;
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
	public boolean hasOperation(Instruction instruct) 
	{
		if (sysDrafter.hasOperation(instruct)) return true;
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
		
		if (sysDrafter.hasOperation(inst))
		{
			Draft d = sysDrafter.draft(tr, defs, allocs, lbls);
			if (sysDrafter.hasError())
			{
				error = sysDrafter.getError();
			}
			return d;
		}
		
		
		
		
		
		return null;
	}

}
