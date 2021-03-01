package com.nullpointerworks.jasm.virtualmachine;

import java.util.List;

public interface BytecodeVirtualMachine extends VirtualMachine
{
	void throwInterrupt(int intcode);
	int addInstruction(int offset, int[] inst);
	void addInstructions(int offset, List<int[]> instructions);
	boolean hasInstruction();
	void nextInstruction();
}
