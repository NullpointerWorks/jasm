package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SystemAssembler implements DraftAssembler
{
	private BuildError error;
	
	public SystemAssembler() 
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
	
	public boolean hasOperation(Instruction instruct)
	{
		if (instruct == Instruction.NOP) return true;
		if (instruct == Instruction.INT) return true;
		return false;
	}
	
	public Draft draft(	Translation translation, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls)
	{
		error = null;
		Draft d = new Draft( translation );
		Instruction inst = translation.getInstruction();
		
		if (inst == Instruction.NOP)
		{
			buildNOP(translation, defs, allocs, lbls, d);
		}
		
		if (inst == Instruction.INT)
		{
			buildINT(translation, defs, allocs, lbls, d);
		}
		
		return d;
	}

	private void buildNOP(	Translation tr, 
							List<Definition> defs, 
							List<Allocation> allocs,
							List<Label> lbls, 
							Draft d) 
	{
		d.addValue( 0 );
	}

	private void buildINT(	Translation tr, 
							List<Definition> defs, 
							List<Allocation> allocs,
							List<Label> lbls, 
							Draft d) 
	{
		d.addValue( 0x01000000 );
		
	}
	
}
