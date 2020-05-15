package com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;

import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class Increment_S implements Instruction
{
	private Select sa;
	
	public Increment_S(Select sa) 
	{
		this.sa=sa;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		int res = prog.getRegister(sa).getValue() + 1;
		prog.setRegister(sa, res);
		setFlags(prog, res);
	}
	
	protected void setFlags(VirtualMachine prog, int res)
	{
		prog.resetFlags();
		if (res==0) prog.setFlag(Select.ZERO, true);
		if (res < 0) prog.setFlag(Select.SIGN, true);
	}
}