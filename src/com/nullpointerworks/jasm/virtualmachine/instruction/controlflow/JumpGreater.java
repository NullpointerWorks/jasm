package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Select;

public class JumpGreater extends Jump
{
	public JumpGreater(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(VirtualMachine prog, int address)
	{
		boolean sign = !prog.getFlag(Select.SIGN).getValue();
		if (sign) prog.setRegister(Select.IP, address);
	}
	
}
