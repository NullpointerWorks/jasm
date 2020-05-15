package com.nullpointerworks.jasm.virtualmachine.instruction.system;

import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Interrupt implements Instruction
{
	private int intcode = 0;
	
	public Interrupt(int code) 
	{
		intcode = code;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		prog.throwInterrupt(intcode);
	}
}
