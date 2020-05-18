package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;

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
