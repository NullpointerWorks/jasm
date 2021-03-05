package com.nullpointerworks.jasm2.vm;

public enum VMException 
{
	/**
	 * thrown when an opcode was selected for execution, but did not match any known instruction
	 */
	VMEX_NO_EXECUTION,
	
	/**
	 * thrown when a flag was requested, but none was found
	 */
	VMEX_NO_FLAG_FOUND,
	
	/**
	 * thrown when a register was requested, but none was found
	 */
	VMEX_NO_REGISTER_FOUND,
	
	/**
	 * thrown when the instruction pointer (IP) has reached the end of the maximum memory size
	 */
	VMEX_MEMORY_OVERFLOW, 
	
	/**
	 * thrown when a value at a specified memory address was not available. for example, when the index is less than zero, or greater or equals to the memory size.
	 */
	VMEX_MEMORY_OUTOFBOUNDS,
	
	/**
	 * thrown when an instruction tries to decipher it's immediate values, but failed to complete
	 */
	VMEX_BAD_INSTRUCTION,
	
}
