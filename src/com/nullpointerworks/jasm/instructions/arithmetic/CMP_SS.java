package com.nullpointerworks.jasm.instructions.arithmetic;

import com.nullpointerworks.jasm.instructions.*;
import com.nullpointerworks.jasm.processor.*;

/*
 * compare
 */
public class CMP_SS extends AbstractInstruction
{
	private Select sa;
	private Select sb;
	
	public CMP_SS(Select sa, Select sb) 
	{
		this.sa=sa;
		this.sb=sb;
	}
	
	@Override
	public void execute(Processor prog)
	{
		Register ra = prog.getRegister(sa);
		Register rb = prog.getRegister(sb);
		int res = ra.getValue() - rb.getValue();
		setFlags(prog,res);
	}
}
