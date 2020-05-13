package com.nullpointerworks.jasm.instruction.dataflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

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
