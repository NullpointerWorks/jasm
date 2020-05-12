package com.nullpointerworks.jasm.instruction;

import com.nullpointerworks.jasm.processor.Processor;

public interface Instruction
{
	void execute(Processor proc);
	default void setAddress(int addr) {}
}
