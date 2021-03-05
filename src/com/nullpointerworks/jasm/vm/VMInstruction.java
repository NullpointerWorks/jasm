package com.nullpointerworks.jasm.vm;

public enum VMInstruction 
{
	/*
	 * (4x) logic
	 */
	OR(0x40),
	AND(0x41),
	XOR(0x42),
	
	/*
	 * (3x) data flow
	 */
	LOAD_RR(0x30),
	LOAD_RV(0x31),
	LOAD_RM(0x32),
	LOAD_MR(0x33),
	PUSH(0x34),
	POP(0x35),
	
	/*
	 * (2x) control flow
	 */
	JMP(0x20),
	JE(0x21),
	JNE(0x22),
	JL(0x23),
	JLE(0x24),
	JG(0x25),
	JGE(0x26),
	CALL(0x27),
	RET(0x28),
	
	/*
	 * (1x) arithmetic
	 */
	ADD_RR(0x10),
	ADD_RV(0x11),
	SUB_RR(0x12),
	SUB_RV(0x13),
	CMP_RR(0x14),
	CMP_RV(0x15),
	SHL(0x16),
	SHR(0x17),
	NEG(0x18),
	INC(0x19),
	DEC(0x1a),
	MUL_RR(0x1b),
	MUL_RV(0x1c),
	DIV_RR(0x1d),
	DIV_RV(0x1e),
	
	/*
	 * (0x) system
	 */
	NOP(0x00),
	INT(0x01);

	private int code = 0x00;
	private VMInstruction(int c) {code = c;}
	public int getCode() {return code;}
}
