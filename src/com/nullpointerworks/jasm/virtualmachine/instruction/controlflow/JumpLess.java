package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class JumpLess extends Jump
{
	public JumpLess(Register a)
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
