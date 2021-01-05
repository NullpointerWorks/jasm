package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Pop_S implements Instruction
{
	private Select s;
	
	public Pop_S(Select s) 
	{
		this.s=s;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		prog.setRegister(s, prog.popStack() );
	}
}
