package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Jump implements Instruction
{
	private int address;
	
	public Jump(int a)
	{
		address = a;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		execute(prog, address);
	}
	
	public void execute(VirtualMachine prog, int address)
	{
		prog.setRegister(Select.IP, address);
	}
}
