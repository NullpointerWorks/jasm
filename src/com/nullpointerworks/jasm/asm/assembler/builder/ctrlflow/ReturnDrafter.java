package com.nullpointerworks.jasm.asm.assembler.builder.ctrlflow;

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
import com.nullpointerworks.jasm.vm.VMInstruction;

public class ReturnDrafter implements Drafter
{
	private final int opcode;
	private BuildError error;
	
	public ReturnDrafter() 
	{
		opcode = VMInstruction.RET.getCode() << 24;
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
		return instruct == ASMInstruction.RET;
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
		d.addValue( opcode );
		return d;
	}
}
