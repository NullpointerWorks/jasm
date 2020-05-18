package com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;

import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Select;

public class Read_SI implements Instruction
{
	private Select sT;
	private int imm;
	
	public Read_SI(Select sT,int imm) 
	{
		this.sT=sT;
		this.imm=imm;
	}

	@Override
	public void execute(VirtualMachine prog)
	{
		int val = prog.readMemory(imm);
		prog.getRegister(sT).setValue(val);
	}
	
}
