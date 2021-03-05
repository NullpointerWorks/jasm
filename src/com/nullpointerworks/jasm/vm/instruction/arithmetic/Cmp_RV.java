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
 * bytes: 		15 r1 r2 ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Subtract a value from the selected register, but discards the result. Processor flags will still be set.
 */
public class Cmp_RV implements Instruction
{
	private final VMInstruction operation = VMInstruction.CMP_RV;
	
	@Override
	public boolean match(int opcode)
	{
		int code = (opcode & 0xff000000)>>24;
		return code == operation.getCode();
	}
	
	@Override
	public void execute(VirtualMachine vm) 
	{
		Register regIP 		= vm.getRegister(VMRegister.REG_IP);
		int instruction 	= vm.getMemory( regIP.getValue() );
		int value 			= vm.getMemory( regIP.getValue()+1 );
		
		int tar1 = (instruction & 0x00ff0000) >> 16;
		VMRegister sel1 = VMRegister.findRegister(tar1);
		if (sel1 == null)
		{
			vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
			return;
		}
		
		int reg1 = vm.getRegister(sel1).getValue();
		int v = reg1 - value;
		
		setFlags(vm, v);
		regIP.addValue(2);
	}
	
	protected void setFlags(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.ZERO).setValue(false);
		vm.getFlag(VMFlag.SIGN).setValue(false);
		
		if (v==0) vm.getFlag(VMFlag.ZERO).setValue(true);
		if (v<0) vm.getFlag(VMFlag.SIGN).setValue(true);
	}
}
