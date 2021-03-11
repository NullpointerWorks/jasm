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
 * instruction: 		ADD 
 * bytes: 		10 r1 r2 ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * </pre>
 * No-operation
 */
public class Add_RR implements Instruction
{
	private final VMInstruction operation = VMInstruction.ADD_RR;
	
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
		
		reg1.addValue( reg2.getValue() );
		
		int v = reg1.getValue();
		VMUtil.checkZeroFlag(vm, v);
		VMUtil.checkSignFlag(vm, v);
		VMUtil.checkOverflowFlag(vm, v);
		regIP.addValue(1);
	}
}
