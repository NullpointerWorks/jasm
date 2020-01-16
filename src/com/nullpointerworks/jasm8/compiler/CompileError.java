package com.nullpointerworks.jasm8.compiler;

public enum CompileError
{
	NO_ERROR(""),
	BAD_LABEL_NAME("Label contains illigal characters."),
	BAD_LABEL_LOCATION("Labels may not be defined within an instruction.")
	
	;
	
	private String desc;
	CompileError(String desc)
	{this.desc=desc;}
	public String getDescription() {return desc;}
}
