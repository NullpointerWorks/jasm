package com.nullpointerworks.jasm.instruction.system;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

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
