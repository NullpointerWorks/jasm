package com.nullpointerworks.jasm.virtualmachine;

public class Flag
{
	private boolean value = false;
	
	public Flag() {}

	public Flag(boolean value)
	{
		this.value=value;
	}

	public boolean getValue()
	{
		return value;
	}

	public void setValue(boolean value)
	{
		this.value = value;
	}
}
