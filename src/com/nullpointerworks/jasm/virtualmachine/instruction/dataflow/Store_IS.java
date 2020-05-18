package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;

public class Store_IS implements Instruction
{
	private Select sS;
	private int imm;
	
	public Store_IS(int imm, Select sS) 
	{
		this.sS=sS;
		this.imm=imm;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		int valS = prog.getRegister(sS).getValue();
		prog.storeMemory(imm, valS);
	}
	
}
