package com.nullpointerworks.jasm.virtualmachine;

import java.util.List;

public class BytecodeVM extends AbstractVirtualMachine implements BytecodeVirtualMachine
{
	private BytecodeParser bytecode;
	
	public BytecodeVM()
	{
		bytecode = new BytecodeParser();
	}
	
	@Override
	public int addInstruction(int offset, int[] inst) 
	{
		int bytes = offset;
		for (int i : inst)
		{
			this.storeMemory(bytes, i);
			bytes++;
		}
		return offset + bytes;
	}
	
	@Override
	public void setInstructions(int offset, List<int[]> instructions) 
	{
		for (int[] i : instructions)
		{
			offset = addInstruction(offset, i);
		}
	}
	
	@Override
	public boolean hasInstruction()
	{
		return true;
	}
	
	@Override
	public void nextInstruction()
	{
		Register reg = getRegister(Select.IP);
		List<Integer> mem = this.getMemory();
		
		int ip = reg.getValue();
		ip = bytecode.execute(this, mem, ip);
		
		reg.setValue(ip);
	}
}
