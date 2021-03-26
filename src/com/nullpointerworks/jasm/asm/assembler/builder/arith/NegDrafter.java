package com.nullpointerworks.jasm.asm.assembler.builder.arith;

import java.util.List;

import static com.nullpointerworks.jasm.asm.AssemblerUtility.setCode;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.Drafter;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.vm.VMInstruction;

public class NegDrafter implements Drafter
{
	private BuildError error;
	
	public NegDrafter() 
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
		return instruct == ASMInstruction.NEG;
	}
	
	public Draft draft(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls)
	{
		error = null;
		Operand op1 = tr.getOperand(0);
		if (!op1.isAddress())
		{
			Draft d = noAddress(tr);
			return d;
		}
		
		setUncaughtArgumentError(tr);
		return null;
	}

	private Draft noAddress(Translation tr)
	{
		Operand op1 = tr.getOperand(0);
		Draft d = new Draft();
		
		if (op1.isRegister())
		{
			setCode(d, VMInstruction.NEG, op1.getRegister());
			return d;
		}
		
		setUncaughtArgumentError(tr);
		return null;
	}
	
	private void setUncaughtArgumentError(Translation tr) 
	{
		setArgumentError(tr, "  Assemble failure: Uncaught argument combination.");
	}
	
	private void setArgumentError(Translation tr, String msg) 
	{
		error = new AssembleError(tr.getSourceCode(), msg);
	}
}
