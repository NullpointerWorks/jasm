package com.nullpointerworks.jasm.virtualmachine;

import java.util.ArrayList;
import java.util.List;

public class InstructionVM extends AbstractVirtualMachine implements InstructionVirtualMachine
{
	private List<Instruction> instructions;
	
	public InstructionVM()
	{
		instructions = new ArrayList<Instruction>();
	}
	
	@Override
	public void addInstructions(List<Instruction> instructions)
	{
		for (Instruction i : instructions)
		{
			addInstruction(i);
		}
	}
	
	@Override
	public void addInstruction(Instruction inst)
	{
		instructions.add(inst);
	}
	
	@Override
	public boolean hasInstruction()
	{
		Register ip = getRegister(Select.IP);
		return !(ip.getValue() >= instructions.size());
	}
	
	@Override
	public void nextInstruction()
	{
		Register ip = getRegister(Select.IP);
		var instruct = instructions.get(ip.getValue());
		ip.setValue( ip.getValue() + 1 ); // set to next instruction
		instruct.execute(this);
	}
}
