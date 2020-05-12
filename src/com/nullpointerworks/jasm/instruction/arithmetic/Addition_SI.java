package com.nullpointerworks.jasm.instruction.arithmetic;

import com.nullpointerworks.jasm.instruction.AbstractInstruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Register;
import com.nullpointerworks.jasm.processor.Select;

public class Addition_SI extends AbstractInstruction
{
	private Select sa;
	private int imm;
	
	public Addition_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}
	
	@Override
	public void execute(Processor prog)
	{
		Register ra = prog.getRegister(sa);
		int res = ra.getValue() + imm;
		prog.setRegister(sa, res);
		setFlags(prog, res);
	}
}
