package com.nullpointerworks.jasm.jasm8.compiler;

public final class CompilerError
{
	public static final CompilerError NO_ERROR = new CompilerError("");
	
	private String desc;
	CompilerError(String desc) {this.desc=desc;}
	public String getDescription() {return desc;}
	
	/**
	 * 
	 */
	public static CompilerError lineError(ProgramCode pc)
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		return new CompilerError("Error on line: "+number+".\nInvalid JASM syntax: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError includeError(ProgramCode pc) 
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		return new CompilerError("Error on line: "+number+".\nBad include syntax: '"+line+"'");
	}
	
	/**
	 * ".equ" may only have a name and value
	 */
	public static CompilerError equateError(ProgramCode pc) 
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		return new CompilerError("Error on line: "+number+".\nBad equate syntax: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError labelError(ProgramCode pc) 
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		return new CompilerError("Error on line: "+number+".\nInvalid label characters used: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError undefinedLabelError(ProgramCode cl) 
	{
		int number = cl.getLineNumber();
		String text = cl.getLineText();
		return new CompilerError("Error on line: "+number+".\nUndefined label: '"+text+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError numberError(ProgramCode cl) 
	{
		int number = cl.getLineNumber();
		String text = cl.getLineText();
		return new CompilerError("Error on line: "+number+".\nInvalid number syntax: '"+text+"'");
	}
	
}
