package com.nullpointerworks.jasm.instructions.arithmetic;

import com.nullpointerworks.jasm.instructions.*;
import com.nullpointerworks.jasm.processor.*;

public class CMP_SI extends AbstractInstruction
{
	private Select sa;
	private int imm;
	
	public CMP_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}
	
	@Override
	public void execute(Processor prog)
	{
		Register ra = prog.getRegister(sa);
		int res = ra.getValue() - imm;
		setFlags(prog,res);
	}
}
