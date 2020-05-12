package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

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
	public void execute(Processor prog)
	{
		execute(prog, addr);
	}
	
	public void execute(Processor prog, int address)
	{
		prog.setRegister(Select.IP, address);
	}
	
}
