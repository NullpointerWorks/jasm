package com.nullpointerworks.jasm.asm.assembler.builder.dataflow;

import java.util.List;

import static com.nullpointerworks.jasm.asm.AssemblerUtility.setCode;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.Drafter;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.vm.VMInstruction;

public class LoadDrafter implements Drafter
{
	private BuildError error;
	
	public LoadDrafter() 
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
		return instruct == ASMInstruction.LOAD;
	}
	
	public Draft draft(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls)
	{
		error = null;
		Draft d = new Draft();
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		
		if (op1.isAddress())
		{
			if (op1.isNumber())
			{
				setCode(d, VMInstruction.LOAD_MR, op2.getRegister(), op1.getInteger());
			}
			if (op1.isRegister())
			{
				if (op2.isRegister())
				{
					setCode(d, VMInstruction.LOAD_RMR, op1.getRegister(), op2.getRegister());
				}
			}
		}
		else
		{
			if (op1.isRegister())
			{
				if (op2.isRegister())
				{
					setCode(d, VMInstruction.LOAD_RR, op1.getRegister(), op2.getRegister());
				}
				if (op2.isNumber()) 
				{
					setCode(d, VMInstruction.LOAD_RV, op1.getRegister(), op2.getInteger());
				}
			}
			if (op1.isNumber())
			{
				if (op2.isRegister())
				{
					setCode(d, VMInstruction.LOAD_MR, op2.getRegister(), op1.getInteger());
				}
			}
		}
		
		return d;
	}
}
