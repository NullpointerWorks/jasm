package com.nullpointerworks.jasm2.vm.instruction.controlflow;

import com.nullpointerworks.jasm2.vm.Instruction;
import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		CALL 
 * bytes: 		27 ?? ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		n/a
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Indiscriminate jumping to an address, but pushing the location of the next instruction on the stack. Sets the instruction pointer(IP) to another address
 */
public class Call implements Instruction
{
	private final VMInstruction operation = VMInstruction.CALL;
	
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
		int value = vm.getMemory( regIP.getValue()+1 );
		
		Register regSP = vm.getRegister(VMRegister.REG_SP);
		regSP.addValue(-1);
		vm.setMemory(regSP.getValue(), regIP.getValue() + 2 );
		
		regIP.setValue(value);
	}
}
