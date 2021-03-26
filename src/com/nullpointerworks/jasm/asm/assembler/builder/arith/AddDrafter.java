package com.nullpointerworks.jasm.asm.assembler.builder.arith;

import java.util.List;

import static com.nullpointerworks.jasm.asm.AssemblerUtility.setCode;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.TranslatorUtility;
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

public class AddDrafter implements Drafter
{
	private BuildError error;
	
	public AddDrafter() 
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
		return instruct == ASMInstruction.ADD;
	}
	
	public Draft draft(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls)
	{
		error = null;
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		
		if (!op1.isAddress())
		{
			if (!op2.isAddress())
			{
				Draft d = noAddress(tr, defs);
				return d;
			}
		}
		
		setUncaughtArgumentError(tr);
		return null;
	}

	private Draft noAddress(Translation tr, 
							List<Definition> defs)
	{
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		Draft d = new Draft();
		
		if (op1.isRegister())
		{
			if (op2.isRegister())
			{
				setCode(d, VMInstruction.ADD_RR, op1.getRegister(), op2.getRegister());
				return d;
			}
			if (op2.isNumber()) 
			{
				setCode(d, VMInstruction.ADD_RV, op1.getRegister(), op2.getInteger());
				return d;
			}
			if (op2.isLabel()) 
			{
				String name = op2.getOperand();
				Definition def = TranslatorUtility.findDefinition(defs, name);
				if (def != null)
				{
					setCode(d, VMInstruction.ADD_RV, op1.getRegister(), def.getNumber() );
					return d;
				}
				
				setArgumentError(tr, "  Only definitions are allowed.");
				return null;
			}
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
