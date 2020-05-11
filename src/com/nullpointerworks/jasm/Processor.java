package com.nullpointerworks.jasm;

import com.nullpointerworks.jasm.compiler.Register;

public interface Processor
{
	String getVersion();
	void cycle();
	int getRegister(Register ra);
}
