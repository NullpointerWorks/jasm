package com.nullpointerworks.jasm.vm;

public enum VMRegister 
{
	REG_IP,
	REG_SP,
	
	REG_A,
	REG_B,
	REG_C,
	REG_D,
	
	REG_0,
	REG_1,
	REG_2,
	REG_3,
	REG_4,
	REG_5,
	REG_6,
	REG_7,
	REG_8,
	REG_9;

	public static VMRegister findRegister(int code)
	{
		VMRegister[] values = VMRegister.values();
		if (code < 0) return null;
		if (code >= values.length) return null;
		return values[code];
	}
}
