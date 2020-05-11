package com.nullpointerworks.jasm.compiler_old;

public interface LogListener 
{
	void print(String msg);
	void println(String msg);
	void error(String msg);
	void save(String path);
}
