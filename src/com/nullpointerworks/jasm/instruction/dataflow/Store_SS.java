package com.nullpointerworks.jasm.instruction.dataflow;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public class Store_SS implements Instruction
{
	private Select sT;
	private Select sS;
	
	public Store_SS(Select sT, Select sS) 
	{
		this.sT=sT;
		this.sS=sS;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		int valS = prog.getRegister(sS).getValue();
		int indexT = prog.getRegister(sT).getValue();
		prog.storeMemory(indexT, valS);
	}
	
}
