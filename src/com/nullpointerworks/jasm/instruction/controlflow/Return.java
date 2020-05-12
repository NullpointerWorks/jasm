package com.nullpointerworks.jasm.instruction.controlflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public class Return implements Instruction
{
	public Return()
	{
		
	}
	
	@Override
	public void execute(Processor prog)
	{
		int address = prog.popStack();
		prog.setRegister(Select.IP, address);
	}
	
}
