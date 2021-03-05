package com.nullpointerworks.jasm.vm;

@FunctionalInterface
public interface InterruptListener 
{
	void onInterrupt(VirtualMachine vm, int code);
}
