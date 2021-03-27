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
	LOAD_RRM(0x31),
	LOAD_RMR(0x32),
	LOAD_RV(0x33),
	LOAD_RM(0x34),
	LOAD_MR(0x35),
	PUSH_R(0x36),
	PUSH_V(0x37),
	POP(0x38),
	LOAD_RMV(0x39),
	
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
	
	/*
	 * (0x) system
	 */
	NOP(0x00),
	INT(0x01);

	private int code = 0x00;
	private VMInstruction(int c) {code = c;}
	public int getCode() {return code;}
}
