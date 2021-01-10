package com.nullpointerworks.jasm.virtualmachine.instruction.logic;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Xor_SI implements Instruction
{
	private Select sa;
	private int imm;
	
	public Xor_SI(Select sa, int imm) 
	{
		this.sa=sa;
		this.imm=imm;
	}
	
	@Override
	public void execute(VirtualMachine prog)
	{
		Register ra = prog.getRegister(sa);
		int res = (ra.getValue() ^ imm);
		prog.setRegister(sa, res);
	}
}
