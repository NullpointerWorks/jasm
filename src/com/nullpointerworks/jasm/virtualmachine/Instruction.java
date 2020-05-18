package com.nullpointerworks.jasm.virtualmachine;

import com.nullpointerworks.jasm.VirtualMachine;

public interface Instruction
{
	void execute(VirtualMachine proc);
	default void setJumpAddress(int addr) {}
}
