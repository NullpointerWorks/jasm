package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Select;

public class JumpLess extends Jump
{
	public JumpLess(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(VirtualMachine prog, int address)
	{
		boolean sign = prog.getFlag(Select.SIGN).getValue();
		if (sign) prog.setRegister(Select.IP, address);
	}
	
}
