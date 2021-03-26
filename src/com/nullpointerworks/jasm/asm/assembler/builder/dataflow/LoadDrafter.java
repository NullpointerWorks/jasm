package com.nullpointerworks.jasm.asm.assembler.builder.dataflow;

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
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		
		if (op1.isAddress())
		{
			if (!op2.isAddress())
			{
				Draft d = firstAddress(tr, defs, allocs, lbls);
				return d;
			}
		}
		else
		{
			if (op2.isAddress())
			{
				Draft d = secondAddress(tr, defs, allocs, lbls);
				return d;
			}
			else
			{
				Draft d = noAddress(tr, defs, allocs, lbls);
				return d;
			}
		}
		
		setUncaughtArgumentError(tr);
		return null;
	}

	private Draft noAddress(Translation tr, 
							List<Definition> defs, 
							List<Allocation> allocs,
							List<Label> lbls)
	{
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		Draft d = new Draft();
		
		if (op1.isRegister())
		{
			if (op2.isRegister())
			{
				setCode(d, VMInstruction.LOAD_RR, op1.getRegister(), op2.getRegister());
				return d;
			}
			if (op2.isNumber()) 
			{
				setCode(d, VMInstruction.LOAD_RV, op1.getRegister(), op2.getInteger());
				return d;
			}
			if (op2.isLabel()) 
			{
				String name = op2.getOperand();
				Definition def = TranslatorUtility.findDefinition(defs, name);
				if (def != null)
				{
					setCode(d, VMInstruction.LOAD_RV, op1.getRegister(), def.getNumber() );
					return d;
				}
				Allocation alloc = TranslatorUtility.findAllocation(allocs, name);
				if (alloc != null)
				{
					setCode(d, VMInstruction.LOAD_RV, op1.getRegister(), alloc.getAddress() );
					return d;
				}
				
				setArgumentError(tr, "  Only definitions and allocation references are allowed.");
				return null;
			}
		}
		
		setUncaughtArgumentError(tr);
		return null;
	}

	private Draft firstAddress(Translation tr, 
								List<Definition> defs, 
								List<Allocation> allocs,
								List<Label> lbls)
	{
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		Draft d = new Draft();
		
		if (op2.isRegister())
		{
			if (op1.isNumber())
			{
				setCode(d, VMInstruction.LOAD_MR, op2.getRegister(), op1.getInteger());
				return d;
			}
			if (op1.isRegister())
			{
				setCode(d, VMInstruction.LOAD_RMR, op2.getRegister(), op1.getRegister());
				return d;
			}
		}
		
		setUncaughtArgumentError(tr);
		return null;
	}
	
	private Draft secondAddress(Translation tr, 
								List<Definition> defs, 
								List<Allocation> allocs,
								List<Label> lbls)
	{
		Operand op1 = tr.getOperand(0);
		Operand op2 = tr.getOperand(1);
		Draft d = new Draft();
		
		if (op1.isRegister())
		{
			if (op2.isNumber()) 
			{
				setCode(d, VMInstruction.LOAD_RM, op1.getRegister(), op2.getInteger());
				return d;
			}
			if (op2.isRegister())
			{
				setCode(d, VMInstruction.LOAD_RRM, op1.getRegister(), op2.getRegister());
				return d;
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
