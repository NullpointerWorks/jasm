package com.nullpointerworks.jasm2.vm.instruction.arithmetic;

import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.VirtualMachine;
import com.nullpointerworks.jasm2.vm.Instruction;

/**
 * <pre>
 * instruction: 		MUL
 * bytes: 		17 r1 ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Multiply the content of a registers with another value and stores the result in register 1.
 */
public class Mul_RV implements Instruction
{
	private final VMInstruction operation = VMInstruction.MUL_RV;
	
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
		
		VMRegister sel1 = VMUtil.getFirstRegister(vm, instruction);
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		
		long result = (long)reg1.getValue() * (long)value;
		int v = (int)(result & 0x00000000ffffffff);
		int o = (int)(result >> 32);
		
		reg1.setValue(v);
		VMUtil.setFlags(vm, v, o);
		regIP.addValue(2);
	}
}
