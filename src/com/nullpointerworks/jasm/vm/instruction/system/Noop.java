package com.nullpointerworks.jasm.vm.instruction.system;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		NOP 
 * bytes: 		00 00 00 00
 * byte 1:		operation code
 * byte 2:		operation code
 * byte 3:		operation code
 * byte 4:		operation code
 * </pre>
 * No-operation
 */
public class Noop implements Instruction
{
	private final VMInstruction operation = VMInstruction.NOP;
	
	@Override
	public boolean match(int opcode)
	{
		return opcode == operation.getCode();
	}
	
	@Override
	public void execute(VirtualMachine vm) 
	{
		Register regIP = vm.getRegister(VMRegister.REG_IP);
		regIP.addValue(1);
	}
}
