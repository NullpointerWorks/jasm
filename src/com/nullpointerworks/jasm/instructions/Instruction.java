package com.nullpointerworks.jasm.instructions;

import com.nullpointerworks.jasm.processor.Processor;

public interface Instruction
{
	void execute(Processor prog);
	default void setAddress(int addr) {}
}
