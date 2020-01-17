package com.nullpointerworks.jasm.jasm8.compiler;

import com.nullpointerworks.jasm.jasm8.parts.InstructionsJASM8;

public class DraftJASM8 implements InstructionsJASM8
{
	private boolean hasLabel = false;
	private String label = "";
	private int code_index = 0;
	private byte[] machine_code;

	public DraftJASM8(int index, byte[] microcode)
	{
		code_index = index;
		machine_code = microcode;
	}
	
	public DraftJASM8(int index, byte[] microcode, String label)
	{
		this.label=label;
		hasLabel = true;
		code_index = index;
		machine_code = microcode;
	}
	
	/*
	 * ===========================================================
	 */

	public final boolean hasLabel() {return hasLabel;}
	public final String getLabel() {return label;}
	public final void setLabelAddress(int addr) 
	{
		short addr16 = (short)addr;
		byte H = (byte)(addr16>>8);
		byte L = (byte)(addr16);
		machine_code[1]=H;
		machine_code[2]=L;
	}
	
	public final int codeIndex() {return code_index;}
	public final byte[] machineCode() {return machine_code;}
	public final String machineCodeToString()
	{
		String hex = "";
		for (byte b : machine_code)
		{
			hex += String.format("%02X ", b);
		}
		return hex;
	}
}
