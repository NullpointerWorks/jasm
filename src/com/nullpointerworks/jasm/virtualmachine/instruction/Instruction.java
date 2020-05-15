package com.nullpointerworks.jasm.virtualmachine.instruction;

import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;

public interface Instruction
{
	void execute(VirtualMachine proc);
	default void setJumpAddress(int addr) {}
}
