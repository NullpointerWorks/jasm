package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.TranslatorUtility;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.Number;

public class SystemDrafter implements Drafter
{
	private BuildError error;
	
	public SystemDrafter() 
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
		Draft d = new Draft();
		Instruction inst = translation.getInstruction();
		
		if (inst == Instruction.NOP)
		{
			buildNOP(d);
		}
		
		if (inst == Instruction.INT)
		{
			buildINT(translation, defs, allocs, lbls, d);
		}
		
		return d;
	}

	private void buildNOP(	Draft d) 
	{
		d.addValue( 0 );
	}

	private void buildINT(	Translation tr, 
							List<Definition> defs, 
							List<Allocation> allocs,
							List<Label> lbls, 
							Draft d) 
	{
		Operand op = tr.getOperand(0);
		if (op.isNumber())
		{
			int num = op.getInteger();
			d.addValue( insert(num) );
		}
		else
		if (op.isLabel())
		{
			String name = op.getOperand();
			Definition def = TranslatorUtility.findDefinition(defs, name);
			if (def!=null)
			{
				int num = def.getNumber().getValue();
				d.addValue( insert(num) );
			}
			else
			{
				error = new AssembleError(tr.getSourceCode(), "  Definition \""+name+"\" cannot be found.");
			}
		}
	}
	
	private int insert(int num) 
	{
		return num = 0x01000000 | (num & 0x00ffffff);
	}
}
