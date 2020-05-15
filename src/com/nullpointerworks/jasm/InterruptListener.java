package com.nullpointerworks.jasm;

public interface InterruptListener
{
	void onInterrupt(VirtualMachine vm, int intcode);
}
