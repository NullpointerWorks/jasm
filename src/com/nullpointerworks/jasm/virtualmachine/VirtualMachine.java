package com.nullpointerworks.jasm.virtualmachine;

import java.util.List;

public interface VirtualMachine 
{
	void throwInterrupt(int intcode);
	
	void addInstruction(Instruction inst);
	void addInstructions(List<Instruction> instructions);
	boolean hasInstruction();
	void nextInstruction();

	void setMemorySize(int size);
	List<Integer> getMemory();
	void storeMemory(int index, int value);
	int readMemory(int index);
	
	void pushStack(int x);
	int popStack();
	
	void resetCounter(int c);
	void setRegister(Select sa, int res);
	Register getRegister(Select sa);
	
	void resetFlags();
	void setFlag(Select s, boolean b);
	Flag getFlag(Select s);
}
