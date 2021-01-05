package com.nullpointerworks.jasm.compiler.errors;

import com.nullpointerworks.jasm.compiler.SourceCode;

public class GenericError implements BuildError
{
	private final SourceCode code;
	private final String desc;
	private final String type;
	
	public GenericError(SourceCode code, String desc, String type)
	{
		this.code=code;
		this.desc=desc;
		this.type=type;
	}
	
	@Override
	public SourceCode getSourceCode() 
	{
		return code;
	}
	
	@Override
	public String getDescription() 
	{
		String msg = type+"\n"+desc;
		if (code!=null)
		{
			msg+=" in "+code.getFilename()+"\n";
			msg+="  Line: "+code.getLinenumber()+"\n";
			msg+="  Code: "+code.getLine()+"\n";
		}
		return msg;
	}
}
