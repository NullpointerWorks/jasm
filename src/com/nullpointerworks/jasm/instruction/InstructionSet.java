package com.nullpointerworks.jasm.instruction;

public enum InstructionSet
{
	NOP,
	INT,
	
	ADD,
	SUB,
	CMP,
	SL,
	SR,
	INC,
	DEC,
	NEG,
	
	LOAD,
	PUSH,
	POP,
	STO,
	READ,
	
	JMP,
	JE,
	JNE,
	JL,
	JLE,
	JG,
	JGE,
	CALL,
	RET;
}
