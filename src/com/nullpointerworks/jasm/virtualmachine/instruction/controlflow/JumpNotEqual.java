package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Select;

public class JumpNotEqual extends Jump
{
	public JumpNotEqual(int a)
	{
		super(a);
	}
	
	@Override
	public void execute(VirtualMachine prog, int address)
	{
		boolean zero = prog.getFlag(Select.ZERO).getValue();
		if (!zero) prog.setRegister(Select.IP, address);
	}
	
}
