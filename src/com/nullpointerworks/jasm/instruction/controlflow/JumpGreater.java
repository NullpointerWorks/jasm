package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public class JumpGreater extends Jump
{
	public JumpGreater(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(Processor prog, int address)
	{
		boolean sign = !prog.getFlag(Select.SIGN).getValue();
		if (sign) prog.setRegister(Select.IP, address);
	}
	
}
