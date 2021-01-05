package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class JumpGreaterEqual extends Jump
{
	public JumpGreaterEqual(Register a)
	{
		super(a);
	}
	
	@Override
	public void execute(VirtualMachine prog, int address)
	{
		boolean zero = prog.getFlag(Select.ZERO).getValue();
		boolean sign = !prog.getFlag(Select.SIGN).getValue();
		if (sign || zero) prog.setRegister(Select.IP, address);
	}
	
}
