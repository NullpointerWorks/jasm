package com.nullpointerworks.jasm.compiler;

public class Operand 
{
	private String value = "";
	
	public Operand() {}

	public Operand(String value)
	{
		this.value=value;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
}
