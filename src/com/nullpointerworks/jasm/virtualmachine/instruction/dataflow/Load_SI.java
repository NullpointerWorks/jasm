package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Load_SI implements Instruction
{
	private Select sa;
	private int imm;
	
	public Load_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		prog.setRegister(sa, imm);
	}
	
}
