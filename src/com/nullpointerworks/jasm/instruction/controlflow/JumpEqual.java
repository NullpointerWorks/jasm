package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class JumpEqual extends Jump
{
	public JumpEqual(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(VirtualMachine prog, int address)
	{
		boolean zero = prog.getFlag(Select.ZERO).getValue();
		if (zero) prog.setRegister(Select.IP, address);
	}
	
}
