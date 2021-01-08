package com.nullpointerworks.jasm.compiler;

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
