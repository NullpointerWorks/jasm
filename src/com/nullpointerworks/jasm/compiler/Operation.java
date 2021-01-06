package com.nullpointerworks.jasm.compiler;

public enum Operation
{
	NOP(0),
	INT(1),
	
	ADD(10),
	SUB(11),
	CMP(12),
	SHL(13),
	SHR(14),
	INC(15),
	DEC(16),
	NEG(17),
	
	LOAD(20),
	PUSH(21),
	POP(22),
	STO(23),
	READ(24),
	
	CALL(30),
	JMP(31),
	JE(32),
	JNE(33),
	JL(34),
	JLE(35),
	JG(36),
	JGE(37),
	RET(38);
	
	private int opcode;
	private Operation(int oc)
	{
		opcode = oc;
	}
	public int getOpCode()
	{
		return opcode;
	}
}
