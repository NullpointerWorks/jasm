package com.nullpointerworks.jasm.compiler;

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
	STO,
	READ,
	
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
