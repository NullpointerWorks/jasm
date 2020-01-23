package com.nullpointerworks.jasm;

public interface Monitor
{
	void interrupt(int code);
	void onEND(int x);
}
