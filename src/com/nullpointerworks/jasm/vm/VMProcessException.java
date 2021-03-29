package com.nullpointerworks.jasm.vm;

public class VMProcessException 
{
	private final VMException vmex;
	private final Class<?> clazz;
	private VirtualMachine vmachine;
	private String message;
	
	public VMProcessException(VMException ex, String msg, Class<?> cl) 
	{
		vmex = ex;
		message = msg;
		clazz = cl;
	}
	
	public VMProcessException(VMException ex, VirtualMachine vm, String msg, Class<?> cl) 
	{
		vmex = ex;
		vmachine = vm;
		message = msg;
		clazz = cl;
	}
	
	public VMException getException()
	{
		return vmex;
	}
	
	public String getMemoryTrace()
	{
		String strException = getException().toString();
		String strClass = clazz.getName();
		String result = "Exception "+strException+
						": "+message+
						"\n    Caused by "+strClass+".";
		
		if (vmachine == null) return result;
		return result; // String.format("%x", y)
	}
}
