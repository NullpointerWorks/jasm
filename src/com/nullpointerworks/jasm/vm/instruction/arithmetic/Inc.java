package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;
import com.nullpointerworks.jasm.vm.instruction.VMUtil;

/*
 * add <reg>,<reg>
 * add <reg>,<imm>
 */

/**
 * <pre>
 * instruction: 		INC 
 * bytes: 		19 r1 r2 ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * </pre>
 * increase the value of a register by 1.
 */
public class Inc implements Instruction
{
	private final VMInstruction operation = VMInstruction.INC;
	
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
		reg1.addValue(1);
		
		int v = reg1.getValue();
		VMUtil.checkZeroFlag(vm, v);
		regIP.addValue(1);
	}
}
