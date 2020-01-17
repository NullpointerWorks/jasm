package com.nullpointerworks.jasm.jasm8;

public interface LogListener 
{
	void print(String msg);
	void println(String msg);
	void error(String msg);
	void save(String path);
}
