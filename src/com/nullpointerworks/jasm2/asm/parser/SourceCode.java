package com.nullpointerworks.jasm2.asm.parser;

/**
 * Represents a single line of code. This data object contains a line of code, line number and file name.
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class SourceCode
{
	private final String filename;
	private final int linenumber;
	private String line;
	
	public SourceCode(String filename, int linenumber, String line)
	{
		this.filename=filename;
		this.linenumber=linenumber;
		this.line=line;
	}
	
	public String getFilename()
	{
		return filename;
	}

	public int getLinenumber()
	{
		return linenumber;
	}
	
	public void setLine(String line)
	{
		this.line=line;
	}

	public String getLine()
	{
		return line;
	}
}
