package com.nullpointerworks.jasm.instruction.system;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class NoOperation implements Instruction
{
	public NoOperation() 
	{
		
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		
	}
}
