package com.nullpointerworks.jasm;

import java.util.List;

import com.nullpointerworks.jasm.virtualmachine.Flag;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;

public interface VirtualMachine 
{
	void throwInterrupt(int intcode);
	
	void addInstruction(Instruction inst);
	void addInstructions(List<Instruction> instructions);
	boolean hasInstruction();
	void nextInstruction();
	
	void setMemory(List<Integer> mem);
	void storeMemory(int index, int value);
	int readMemory(int index);
	void pushStack(int x);
	int popStack();
	
	void resetCounter(int c);
	void setRegister(Select sa, int res);
	Register getRegister(Select sa);
	
	void resetFlags();
	void setFlag(Select zero, boolean b);
	Flag getFlag(Select s);
}
