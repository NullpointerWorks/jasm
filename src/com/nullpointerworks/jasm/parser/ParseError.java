package com.nullpointerworks.jasm.parser;

public class ParseError extends SourceCode
{
	private final String desc;
	
	public ParseError(String filename, int linenumber, String line, String desc)
	{
		super(filename, linenumber, line);
		this.desc=desc;
	}
	
	public String getDescription() {return desc;}
}
