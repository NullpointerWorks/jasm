package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.parser.SourceCode;

public class CompileError extends SourceCode
{
	private final String desc;
	
	public CompileError(String filename, int linenumber, String line, String desc)
	{
		super(filename, linenumber, line);
		this.desc=desc;
	}
	
	public String getDescription() 
	{
		String msg = 
		this.getFilename() + " - Line "+this.getLinenumber() + "\n" + 
		"Error  : "+desc+"\n"+
		"Code   : "+this.getLine()+"\n";
		return msg;
	}
}
