package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public class JumpLessEqual extends Jump
{
	public JumpLessEqual(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(Processor prog, int address)
	{
		boolean zero = prog.getFlag(Select.ZERO).getValue();
		boolean sign = prog.getFlag(Select.SIGN).getValue();
		if (sign || zero) prog.setRegister(Select.IP, address);
	}
	
}
