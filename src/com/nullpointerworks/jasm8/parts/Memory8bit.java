package com.nullpointerworks.jasm8.parts;

import com.nullpointerworks.jasm8.Memory;

/*
 * byte memory module
 */
public class Memory8bit implements Memory
{
	private final byte[] mem;
	
	public Memory8bit(int bytes)
	{
		mem = new byte[bytes];
		for (int i=0,l=bytes;i<l;i++) mem[i]=-1;
	}
	
	public final int length()
	{
		return mem.length;
	}
	
	public final byte read(int address)
	{
		return mem[address];
	}
	
	public final Memory write(int address, byte data)
	{
		mem[address] = data;
		return this;
	}
	
	public final Memory load(byte[] program)
	{
		if (program==null) return this;
		load(0,program);
		return this;
	}
	
	public final Memory load(int offset, byte[] program)
	{
		if (program.length > mem.length) return this;
		int i = 0;
		int j = offset;
		int l = program.length;
		if ( (j+program.length) > mem.length) return this;
		for (; j<l; i++, j++)
		{
			mem[j] = program[i];
		}
		return this;
	}
	
	public final Memory copy()
	{
		Memory m = new Memory8bit(mem.length).load(mem);
		return m;
	}
}
