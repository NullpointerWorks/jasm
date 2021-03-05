package com.nullpointerworks.jasm.vm;

public interface Instruction 
{
	boolean match(int opcode);
	void execute(VirtualMachine vm);
}
