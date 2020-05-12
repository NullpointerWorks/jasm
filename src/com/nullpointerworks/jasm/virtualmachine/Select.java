package com.nullpointerworks.jasm.virtualmachine;

public enum Select
{
	/*
	 * registers
	 */
	IMM, // immediate
	IP, // instruction pointer
	SP, // stack pointer
	REG_A, // register a
	REG_B, // register b
	REG_C, // register c
	REG_D, // register d
	
	/*
	 * flags
	 */
	ZERO,
	SIGN
	
	
}
