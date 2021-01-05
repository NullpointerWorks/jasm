package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

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
