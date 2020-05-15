package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Push_I implements Instruction
{
	private int imm;
	
	public Push_I(int imm) 
	{
		this.imm=imm;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		prog.pushStack(imm);
	}
}
