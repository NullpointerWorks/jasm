package com.nullpointerworks.jasm.instruction.dataflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public class Load_SI implements Instruction
{
	private Select sa;
	private int imm;
	
	public Load_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}

	@Override
	public void execute(Processor prog)
	{
		prog.setRegister(sa, imm);
	}
	
}
