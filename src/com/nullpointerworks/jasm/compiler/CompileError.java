package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.parser.SourceCode;

public class CompileError implements BuildError
{
	private SourceCode sc;
	private final String desc;
	
	public CompileError(String filename, int linenumber, String line, String desc)
	{
		sc = new SourceCode(filename, linenumber, line);
		this.desc=desc;
	}
	
	public CompileError(SourceCode sc, String desc)
	{
		this.sc=sc;
		this.desc=desc;
	}
	
	public SourceCode getSourceCode()
	{
		return sc;
	}
	
	public String getDescription() 
	{
		String msg = 
		sc.getFilename() + " - Line "+sc.getLinenumber() + "\n" + 
		"Error  : "+desc+"\n"+
		"Code   : "+sc.getLine()+"\n";
		return msg;
	}
}
