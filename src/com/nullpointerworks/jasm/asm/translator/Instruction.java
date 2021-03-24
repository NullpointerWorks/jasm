package com.nullpointerworks.jasm.asm.translator;

public enum Instruction 
{
	NULL,
	
	NOP,
	INT,
	
	LOAD,
	PUSH,
	POP,
	
	ADD,
	SUB,
	CMP,
	INC,
	DEC,
	SHL,
	SHR,
	NEG,
	
	CALL,
	RET,
	JMP,
	JE,
	JNE,
	JL,
	JLE,
	JG,
	JGE
}
