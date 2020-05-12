package com.nullpointerworks.jasm.processor;

import java.util.List;

import com.nullpointerworks.jasm.instruction.Instruction;

public interface Processor 
{
	void resetCounter(int c);
	void resetFlags();
	
	void setFlag(Select zero, boolean b);
	Flag getFlag(Select s);

	void setRegister(Select sa, int res);
	Register getRegister(Select sa);
	
	void throwInterrupt(int intcode);
	void pushStack(int x);
	int popStack();
	
	void addInstruction(Instruction inst);
	void addInstructions(List<Instruction> instructions);
	boolean hasInstruction();
	void nextInstruction();
}
