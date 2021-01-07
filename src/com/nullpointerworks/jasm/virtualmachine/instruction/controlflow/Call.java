package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Call extends Jump
{
	public Call(int a) 
	{
		super(a);
	}
	
	@Override
	public void execute(VirtualMachine prog, int address)
	{
		Register ip = prog.getRegister(Select.IP);
		prog.pushStack(ip.getValue());
		ip.setValue(address);
	}
	
}
