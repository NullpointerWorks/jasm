package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

/**
 * legacy instruction. READ is now defined under LOAD
 */
public class Read_SS implements Instruction
{
	private Select sT;
	private Select sS;
	
	public Read_SS(Select sT, Select sS) 
	{
		this.sT=sT;
		this.sS=sS;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		int val = prog.getRegister(sS).getValue();
		val = prog.readMemory(val);
		prog.getRegister(sT).setValue(val);
	}
}
