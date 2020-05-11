package com.nullpointerworks.jasm2;

public interface Instruction
{
	void execute(Program prog);
	default void setAddress(int addr) {}
}
