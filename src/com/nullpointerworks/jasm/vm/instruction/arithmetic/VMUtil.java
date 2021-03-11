package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.VMException;
import com.nullpointerworks.jasm.vm.VMFlag;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

class VMUtil 
{
	public static void checkZeroFlag(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.ZERO).setValue(false);
		if (v==0) vm.getFlag(VMFlag.ZERO).setValue(true);
	}
	
	public static void checkSignFlag(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.SIGN).setValue(false);
		if (v<0) vm.getFlag(VMFlag.SIGN).setValue(true);
	}
	
	public static void checkOverflowFlag(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.FLOW).setValue(false);
		if (v!=0) vm.getFlag(VMFlag.FLOW).setValue(true);
	}
	
	public static VMRegister getFirstRegister(VirtualMachine vm, int instruction) 
	{
		int tar1 = (instruction & 0x00ff0000) >> 16;
		VMRegister sel1 = VMRegister.findRegister(tar1);
		if (sel1 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
		return sel1;
	}
	
	public static VMRegister getSecondRegister(VirtualMachine vm, int instruction) 
	{
		int tar1 = (instruction & 0x0000ff00) >> 8;
		VMRegister sel1 = VMRegister.findRegister(tar1);
		if (sel1 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
		return sel1;
	}
	
	public static VMRegister getThirdRegister(VirtualMachine vm, int instruction) 
	{
		int tar1 = (instruction & 0x000000ff);
		VMRegister sel1 = VMRegister.findRegister(tar1);
		if (sel1 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
		return sel1;
	}
}
