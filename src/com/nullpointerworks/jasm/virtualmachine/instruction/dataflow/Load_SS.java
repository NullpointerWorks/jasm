package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Load_SS implements Instruction
{
	private Select sa;
	private Select sb;
	
	public Load_SS(Select sa, Select sb) 
	{
		this.sa=sa;
		this.sb=sb;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		Register rb = prog.getRegister(sb);
		prog.setRegister(sa, rb.getValue());
	}
	
}
