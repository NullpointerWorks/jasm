package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Push_S implements Instruction
{
	private Select s;
	
	public Push_S(Select s) 
	{
		this.s=s;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		Register r = prog.getRegister(s);
		prog.pushStack(r.getValue());
	}
}
