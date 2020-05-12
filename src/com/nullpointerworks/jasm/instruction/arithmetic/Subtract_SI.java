package com.nullpointerworks.jasm.instruction.arithmetic;

import com.nullpointerworks.jasm.instruction.*;
import com.nullpointerworks.jasm.processor.*;

public class Subtract_SI extends AbstractInstruction
{
	private Select sa;
	private int imm;
	
	public Subtract_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}
	@Override
	public void execute(Processor prog)
	{
		Register ra = prog.getRegister(sa);
		int res = ra.getValue() - imm;
		prog.setRegister(sa, res);
		setFlags(prog,res);
	}
}
