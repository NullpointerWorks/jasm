package com.nullpointerworks.jasm.virtualmachine;

@FunctionalInterface
public interface InterruptListener
{
	void onInterrupt(VirtualMachine vm, int intcode);
}
