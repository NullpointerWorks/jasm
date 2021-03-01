package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

/**
 * legacy instruction. STO is now defined under LOAD
 */
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
