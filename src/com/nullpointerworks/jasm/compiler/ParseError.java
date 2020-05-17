package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.BuildError;

public class ParseError implements BuildError
{
	private final SourceCode sc;
	private final String desc;
	
	public ParseError(SourceCode sc, String desc)
	{
		this.sc=sc;
		this.desc=desc;
	}
	
	public ParseError(String desc)
	{
		this.sc=null;
		this.desc=desc;
	}

	public SourceCode getSourceCode() {return sc;}
	
	public String getDescription() 
	{
		String msg = 
		"Parse Error:\n"+desc;
		
		if (sc!=null)
		{
			msg+=" in "+sc.getFilename()+"\n";
			msg+="  Line: "+sc.getLinenumber()+"\n";
			msg+="  Code: "+sc.getLine()+"\n";
			
		}
		
		return msg;
	}
}
