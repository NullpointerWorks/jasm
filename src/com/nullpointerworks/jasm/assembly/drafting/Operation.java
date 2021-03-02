package com.nullpointerworks.jasm.assembly.drafting;

public enum Operation 
{
	NOP,
	INT,
	
	ADD,
	SUB,
	CMP,
	SHL,
	SHR,
	INC,
	DEC,
	NEG,
	
	LOAD,
	PUSH,
	POP,
	
	CALL,
	JMP,
	JE,
	JNE,
	JL,
	JLE,
	JG,
	JGE,
	RET
}