package com.nullpointerworks.jasm.vm.instruction.logic;

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
 * instruction: 		OR
 * bytes: 		40 r1 r2 ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * </pre>
 * Logical OR operation.
 */
public class Or_RR implements Instruction
{
	private final VMInstruction operation = VMInstruction.OR;
	
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
		VMRegister sel2 = VMUtil.getSecondRegister(vm, instruction);
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		Register reg2 = vm.getRegister(sel2);
		int v1 = reg1.getValue();
		int v2 = reg2.getValue();
		reg1.setValue( v1 | v2);
		
		int v = reg1.getValue();
		VMUtil.setFlags(vm, v);
		regIP.addValue(1);
	}
}
