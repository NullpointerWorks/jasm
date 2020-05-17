package com.nullpointerworks.jasm.compiler;

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
