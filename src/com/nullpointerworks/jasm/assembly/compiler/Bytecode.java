package com.nullpointerworks.jasm.assembly.compiler;

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
