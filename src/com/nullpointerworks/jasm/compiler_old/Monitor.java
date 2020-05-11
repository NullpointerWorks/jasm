package com.nullpointerworks.jasm.compiler_old;

public interface Monitor
{
	void onInterrupt(Processor prog, int code);
}
