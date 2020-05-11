package com.nullpointerworks.jasm.instructions.arithmetic;

import com.nullpointerworks.jasm.instructions.AbstractInstruction;
import com.nullpointerworks.jasm.processor.Processor;
import com.nullpointerworks.jasm.processor.Register;
import com.nullpointerworks.jasm.processor.Select;

public class ADD_SI extends AbstractInstruction
{
	private Select sa;
	private int imm;
	
	public ADD_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}
	
	@Override
	public void execute(Processor prog)
	{
		Register ra = prog.getRegister(sa);
		int res = ra.getValue() + imm;
		prog.setSelection(sa, res);
		setFlags(prog, res);
	}
}
