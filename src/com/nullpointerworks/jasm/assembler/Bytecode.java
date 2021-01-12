package com.nullpointerworks.jasm.assembler;

public class Bytecode 
{
	private final String bytecode;
	
	public Bytecode(String bc)
	{
		bytecode = bc;
	}
	
	public String getBytecode()
	{
		return bytecode;
	}
}
