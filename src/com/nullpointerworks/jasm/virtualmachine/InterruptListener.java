package com.nullpointerworks.jasm.virtualmachine;

public interface InterruptListener
{
	void onInterrupt(VirtualMachine vm, int intcode);
}
