package com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Jump implements Instruction
{
	private int addr = 0;
	
	public Jump(int a)
	{
		setJumpAddress(a);
	}

	@Override
	public void setJumpAddress(int a)
	{
		addr = a;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		execute(prog, addr);
	}
	
	public void execute(VirtualMachine prog, int address)
	{
		prog.setRegister(Select.IP, address);
	}
	
}
