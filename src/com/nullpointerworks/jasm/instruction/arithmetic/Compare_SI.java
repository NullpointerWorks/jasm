package com.nullpointerworks.jasm.instruction.arithmetic;

import com.nullpointerworks.jasm.instruction.*;
import com.nullpointerworks.jasm.processor.*;

public class Compare_SI implements Instruction
{
	private Select sa;
	private int imm;
	
	public Compare_SI(Select sa, int imm) 
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
	
	protected void setFlags(Processor prog, int res)
	{
		prog.resetFlags();
		if (res==0) prog.setFlag(Select.ZERO, true);
		if (res < 0) prog.setFlag(Select.SIGN, true);
	}
}
