package com.nullpointerworks.jasm.compiler_old;

public interface Processor
{
	String getVersion();
	void cycle();
	int getRegister(Register ra);
}
