package com.nullpointerworks.jasm.instruction;

import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public interface Instruction
{
	void execute(VirtualMachine proc);
	default void setJumpAddress(int addr) {}
}
