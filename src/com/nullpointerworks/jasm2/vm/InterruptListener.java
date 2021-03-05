package com.nullpointerworks.jasm2.vm;

@FunctionalInterface
public interface InterruptListener 
{
	void onInterrupt(VirtualMachine vm, int code);
}
