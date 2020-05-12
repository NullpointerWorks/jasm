package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public class JumpNotEqual implements Instruction
{
	private int addr = 0;
	
	public JumpNotEqual(int a)
	{
		addr = a;
	}
	
	@Override
	public void execute(Processor prog)
	{
		boolean zero = prog.getFlag(Select.ZERO).getValue();
		if (!zero) prog.setRegister(Select.IP, addr);
	}
	
}
