package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Register;
import com.nullpointerworks.jasm.processor.Select;

public class Call extends Jump
{
	public Call(int a) 
	{
		super(a);
	}
	
	@Override
	public void execute(Processor prog, int address)
	{
		Register ip = prog.getRegister(Select.IP);
		prog.pushStack(ip.getValue());
		ip.setValue(address);
	}
	
}
