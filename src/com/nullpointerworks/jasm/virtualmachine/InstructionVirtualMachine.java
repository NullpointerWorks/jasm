package com.nullpointerworks.jasm.virtualmachine;

import java.util.List;

public interface InstructionVirtualMachine extends VirtualMachine
{
	void throwInterrupt(int intcode);
	void addInstruction(Instruction inst);
	void setInstructions(List<Instruction> instructions);
	boolean hasInstruction();
	void nextInstruction();
}
