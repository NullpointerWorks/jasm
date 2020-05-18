package com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.*;

public class Subtract_SI implements Instruction
{
	private Select sa;
	private int imm;
	
	public Subtract_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}
	@Override
	public void execute(VirtualMachine prog)
	{
		Register ra = prog.getRegister(sa);
		int res = ra.getValue() - imm;
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
