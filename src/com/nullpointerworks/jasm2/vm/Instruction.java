package com.nullpointerworks.jasm2.vm;

public interface Instruction 
{
	boolean match(int opcode);
	void execute(VirtualMachine vm);
}
