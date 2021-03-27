package com.nullpointerworks.jasm.vm;

import java.util.List;

public interface VirtualMachine 
{
	void nextInstruction();
	void throwInterrupt(int code);
	
	void addInstruction(Instruction inst);
	
	void setInterruptListener(InterruptListener il);
	void setOrigin(int origin);
	void setMemorySize(int size);
	void setMemory(int index, int value);
	void setMemory(int offset, List<Integer> mem);
	
	Register getRegister(VMRegister sel);
	Flag getFlag(VMFlag sel);
	int getMemory(int index);
	int getMemoryAt(Register reg);
	
	void throwException(VMException excode);
	void throwException(VMException excode, Class<?> clazz);
	void throwException(VMProcessException vmex);
	boolean hasException();
	VMProcessException getException();
	
}
