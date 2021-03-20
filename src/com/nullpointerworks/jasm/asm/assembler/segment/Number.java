package com.nullpointerworks.jasm.asm.assembler.segment;

public class Number 
{
	private int value = 0;
	
	public Number() {}

	public Number(int value)
	{
		this.value=value;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public void addValue(int i) 
	{
		value = value + i;
	}
}
