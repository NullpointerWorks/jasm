package com.nullpointerworks.jasm8;

public interface Memory
{
	/**
	 * old-school kilobyte (1024). now kibibyte
	 */
	public static final int kiloByte = 1024;
	
	/**
	 * old-school megabyte (1024^2 = 1048576). now megibyte
	 */
	public static final int megaByte = 1024*kiloByte;

	int length();
	byte read(int address);
	Memory write(int address, byte data);
	Memory load(byte[] program);
	Memory load(int offset, byte[] program);
	Memory copy();
}
