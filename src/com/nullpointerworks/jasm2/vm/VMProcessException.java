package com.nullpointerworks.jasm2.vm;

public class VMProcessException 
{
	private VMException vmex;
	private VirtualMachine vmachine;
	private int index;
	
	public VMProcessException(VMException ex) 
	{
		vmex = ex;
		vmachine = null;
		index = -1;
	}
	
	public VMProcessException(VMException ex, VirtualMachine vm, int i) 
	{
		vmex = ex;
		vmachine = vm;
		index = i;
	}
	
	public VMException getException()
	{
		return vmex;
	}
	
	public String getMemoryTrace()
	{
		String result = getException().toString();
		if (vmachine == null) return result;
		
		
		
		return result; // String.format("%x", y)
	}
}
