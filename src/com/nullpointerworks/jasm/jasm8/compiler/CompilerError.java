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
	public static CompilerError duplicateLabelError(SourceCode sc)
	{
		int number = sc.getLineNumber();
		String line = sc.getLineText();
		String file = sc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Duplicate label defined: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError syntaxError(SourceCode pc)
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		String file = pc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Invalid JASM syntax: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError includeError(SourceCode pc) 
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		String file = pc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Bad include syntax: '"+line+"'");
	}
	
	/**
	 * ".equ" may only have a name and value
	 */
	public static CompilerError equateError(SourceCode pc) 
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		String file = pc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Bad equate syntax: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError labelError(SourceCode pc) 
	{
		int number = pc.getLineNumber();
		String line = pc.getLineText();
		String file = pc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Invalid label characters used: '"+line+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError undefinedLabelError(SourceCode sc) 
	{
		int number = sc.getLineNumber();
		String text = sc.getLineText();
		String file = sc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Undefined label: '"+text+"'");
	}
	
	/**
	 * 
	 */
	public static CompilerError numberError(SourceCode sc) 
	{
		int number = sc.getLineNumber();
		String text = sc.getLineText();
		String file = sc.getSourceFile();
		return new CompilerError("Error in file: "+file+"\n"
								+ "Error on line: "+number+"\n"
								+ "Invalid number syntax: '"+text+"'");
	}
	
}
