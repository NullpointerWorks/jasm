package com.nullpointerworks.jasm2.vm.instruction.system;

import com.nullpointerworks.jasm2.vm.Instruction;
import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		INT 
 * bytes: 		01 xx xx xx
 * byte 1:		operation code
 * byte 2:		value
 * byte 3:		value
 * byte 4:		value
 * </pre>
 * VM execution interrupt code range: [0 - 16,777,215]
 */
public class Interrupt implements Instruction
{
	private final VMInstruction operation = VMInstruction.INT;
	
	@Override
	public boolean match(int opcode)
	{
		int code = (opcode & 0xff000000)>>24; // only care about the two most significant bytes
		return code == operation.getCode();
	}
	
	@Override
	public void execute(VirtualMachine vm) 
	{
		Register regIP = vm.getRegister(VMRegister.REG_IP);
		int memory  = vm.getMemoryAt( regIP );
		int intCode = memory & 0x00ffffff;
		vm.throwInterrupt(intCode);
		regIP.addValue(1);
	}
}
