package com.nullpointerworks.jasm;

public interface Monitor
{
	void onInterrupt(Processor prog, int code);
}
