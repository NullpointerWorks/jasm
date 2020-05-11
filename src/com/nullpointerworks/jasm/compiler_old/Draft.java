package com.nullpointerworks.jasm.compiler_old;

public class Draft implements InstructionsJASM
{
	private boolean hasLabel = false;
	private String label = "";
	private SourceCode codeline;
	private byte[] machine_code;
	
	public Draft(byte[] microcode)
	{
		machine_code = microcode;
	}
	
	public Draft(byte[] microcode, String label)
	{
		machine_code = microcode;
		this.label=label;
		hasLabel = true;
	}
	
	/*
	 * ===========================================================
	 */
	
	public final void setSourceCode(SourceCode cl) {codeline = cl;}
	public final SourceCode getSourceCode() {return codeline;}

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
