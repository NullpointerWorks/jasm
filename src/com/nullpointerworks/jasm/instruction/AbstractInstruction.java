package com.nullpointerworks.jasm.instruction;

import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Select;

public abstract class AbstractInstruction implements Instruction
{
	public abstract void execute(Processor prog);
	
	protected void setFlags(Processor prog, int res)
	{
		prog.resetFlags();
		if (res==0) prog.setFlag(Select.ZERO, true);
		if (res < 0) prog.setFlag(Select.SIGN, true);
	}
}
