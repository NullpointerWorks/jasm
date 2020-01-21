package com.nullpointerworks.jasm.jasm8.compiler;

public final class CompileError
{
	public static final CompileError NO_ERROR = new CompileError("");
	
	private String desc;
	CompileError(String desc) {this.desc=desc;}
	public String getDescription() {return desc;}
	
	/**
	 * 
	 */
	public static CompileError lineError(int number, String line)
	{
		return new CompileError("Error on line: "+number+".\nInvalid JASM syntax: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompileError includeError(int number, String s) 
	{
		return new CompileError("Error on line: "+number+".\nBad include syntax: '"+s+"'");
	}
	
	/**
	 * ".equ" may only have a name and value
	 */
	public static CompileError equateError(int number, String s) 
	{
		return new CompileError("Bad equate syntax: '"+s+"'");
	}
	
	/**
	 * 
	 */
	public static CompileError labelError(int number, String s) 
	{
		return new CompileError("Invalid label characters used: '"+s+"'");
	}
	
	/**
	 * 
	 */
	public static CompileError numberError(int number, String s) 
	{
		return new CompileError("Invalid number syntax: '"+s+"'");
	}
	
}
