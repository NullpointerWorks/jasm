package com.nullpointerworks.jasm.instruction.dataflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Register;
import com.nullpointerworks.jasm.processor.Select;

public class Load_SS implements Instruction
{
	private Select sa;
	private Select sb;
	
	public Load_SS(Select sa, Select sb) 
	{
		this.sa=sa;
		this.sb=sb;
	}

	@Override
	public void execute(Processor prog)
	{
		Register rb = prog.getRegister(sb);
		prog.setRegister(sa, rb.getValue());
	}
	
}
