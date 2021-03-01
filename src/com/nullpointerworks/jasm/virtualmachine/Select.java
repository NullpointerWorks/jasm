package com.nullpointerworks.jasm.virtualmachine;

public enum Select
{
	/*
	 * special registers
	 */
	IP, // instruction pointer
	SP, // stack pointer
	
	/*
	 * general purpose registers
	 */
	REG_A, // register a
	REG_B, // register b
	REG_C, // register c
	REG_D, // register d
	
	REG_I, // register i
	REG_J, // register j
	REG_K, // register k
	
	REG_U, // register u
	REG_V, // register v
	REG_W, // register w
	
	REG_X, // register x
	REG_Y, // register y
	REG_Z, // register z
	
	/*
	 * status flags
	 */
	ZERO,
	SIGN
	
	
}
