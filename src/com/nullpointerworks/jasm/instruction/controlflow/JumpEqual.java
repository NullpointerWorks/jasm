package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public class JumpEqual extends Jump
{
	public JumpEqual(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(Processor prog, int address)
	{
		boolean zero = prog.getFlag(Select.ZERO).getValue();
		if (zero) prog.setRegister(Select.IP, address);
	}
	
}
