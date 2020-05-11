package com.nullpointerworks.jasm.preprocessor;

public enum DraftError
{
	NO_ERROR("");
	
	private String desc;
	DraftError(String desc) {this.desc=desc;}
	public String getDescription() {return desc;}
}
