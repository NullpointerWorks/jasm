package com.nullpointerworks.jasm.instruction.dataflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Register;
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
		Register r = prog.getRegister(s);
		r.setValue( prog.popStack() );
	}
}
