package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/*
 * add <reg>,<reg>
 * add <reg>,<imm>
 */

/**
 * <pre>
 * instruction: 		SHR 
 * bytes: 		17 r1 ?? ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * </pre>
 * Performs a bit-shift to the left.
 */
public class Shr implements Instruction
{
	private final VMInstruction operation = VMInstruction.SHR;
	
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
		
		VMRegister sel1 = VMUtil.getFirstRegister(vm, instruction);
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		reg1.setValue( reg1.getValue() >> 1 );
		
		int v = reg1.getValue();
		VMUtil.checkZeroFlag(vm, v);
		VMUtil.checkSignFlag(vm, v);
		VMUtil.checkOverflowFlag(vm, v);
		regIP.addValue(1);
	}
}
