package com.nullpointerworks.jasm.instruction.dataflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Register;
import com.nullpointerworks.jasm.processor.Select;

public class Push_S implements Instruction
{
	private Select s;
	
	public Push_S(Select s) 
	{
		this.s=s;
	}

	@Override
	public void execute(Processor prog)
	{
		Register r = prog.getRegister(s);
		prog.pushStack(r.getValue());
	}
}
