package com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.*;

public class Addition_SS implements Instruction
{
	private Select sa;
	private Select sb;
	
	public Addition_SS(Select sa, Select sb) 
	{
		this.sa=sa;
		this.sb=sb;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		Register ra = prog.getRegister(sa);
		Register rb = prog.getRegister(sb);
		int res = ra.getValue() + rb.getValue();
		prog.setRegister(sa, res);
		setFlags(prog,res);
	}
	
	protected void setFlags(VirtualMachine prog, int res)
	{
		prog.resetFlags();
		if (res==0) prog.setFlag(Select.ZERO, true);
		if (res < 0) prog.setFlag(Select.SIGN, true);
	}
}
