package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Jump implements Instruction
{
	private Register address;
	
	public Jump(Register a)
	{
		address = a;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		execute(prog, address.getValue());
	}
	
	public void execute(VirtualMachine prog, int address)
	{
		prog.setRegister(Select.IP, address);
	}
}
