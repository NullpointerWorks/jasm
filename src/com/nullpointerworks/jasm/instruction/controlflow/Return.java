package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Return implements Instruction
{
	public Return()
	{
		
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		int address = prog.popStack();
		prog.setRegister(Select.IP, address);
	}
	
}
