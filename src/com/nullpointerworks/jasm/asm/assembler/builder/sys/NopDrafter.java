package com.nullpointerworks.jasm.asm.assembler.builder.sys;

import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.Drafter;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class NopDrafter implements Drafter
{
	private BuildError error;
	
	public NopDrafter() 
	{
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
	
	public boolean isInstruction(ASMInstruction instruct)
	{
		return instruct == ASMInstruction.NOP;
	}
	
	public Draft draft(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls)
	{
		error = null;
		
		int s = tr.getOperands().size();
		if (s>0)
		{
			error = new AssembleError(tr.getSourceCode(), "  Assemble failure: Uncaught argument combination.");
		}
		
		Draft d = new Draft();
		d.addValue( 0 );
		return d;
	}
}
