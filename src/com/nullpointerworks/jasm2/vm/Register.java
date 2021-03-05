package com.nullpointerworks.jasm2.vm;

public class Register 
{
	private int value = 0;
	
	public Register() {}

	public Register(int value)
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
