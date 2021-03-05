package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMException;
import com.nullpointerworks.jasm.vm.VMFlag;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		CMP
 * bytes: 		14 r1 r2 ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * </pre>
 * Subtracts two registers but discards the result. Processor flags will still be set.
 */
public class Cmp_RR implements Instruction
{
	private final VMInstruction operation = VMInstruction.CMP_RR;
	
	@Override
	public boolean match(int opcode)
	{
		int code = (opcode & 0xff000000)>>24;
		return code == operation.getCode();
	}
	
	@Override
	public void execute(VirtualMachine vm) 
	{
		Register regIP = vm.getRegister(VMRegister.REG_IP);
		int instruction = vm.getMemoryAt( regIP );
		
		int tar1 = (instruction & 0x00ff0000) >> 16;
		int tar2 = (instruction & 0x0000ff00) >> 8;
		
		VMRegister sel1 = VMRegister.findRegister(tar1);
		VMRegister sel2 = VMRegister.findRegister(tar2);
		
		if (sel1 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
		if (sel2 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
		if (vm.hasException()) return;
		
		int reg1 = vm.getRegister(sel1).getValue();
		int reg2 = vm.getRegister(sel2).getValue();
		int v = reg1 - reg2;
		
		setFlags(vm, v);
		regIP.addValue(1);
	}
	
	protected void setFlags(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.ZERO).setValue(false);
		vm.getFlag(VMFlag.SIGN).setValue(false);
		
		if (v==0) vm.getFlag(VMFlag.ZERO).setValue(true);
		if (v<0) vm.getFlag(VMFlag.SIGN).setValue(true);
	}
}
