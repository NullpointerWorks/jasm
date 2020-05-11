package com.nullpointerworks.jasm.instructions.arithmetic;

import com.nullpointerworks.jasm.instructions.*;
import com.nullpointerworks.jasm.processor.*;

public class ADD_SS extends AbstractInstruction
{
	private Select sa;
	private Select sb;
	
	public ADD_SS(Select sa, Select sb) 
	{
		this.sa=sa;
		this.sb=sb;
	}
	
	@Override
	public void execute(Processor prog)
	{
		Register ra = prog.getRegister(sa);
		Register rb = prog.getRegister(sb);
		int res = ra.getValue() + rb.getValue();
		prog.setSelection(sa, res);
		setFlags(prog,res);
	}
}
