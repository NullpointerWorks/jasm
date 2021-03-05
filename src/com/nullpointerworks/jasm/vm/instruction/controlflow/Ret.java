package com.nullpointerworks.jasm.vm.instruction.controlflow;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		RET 
 * bytes: 		28 ?? ?? ??
 * byte 1:		operation code
 * byte 2:		n/a
 * byte 3:		n/a
 * byte 4:		n/a
 * </pre>
 * Indiscriminate jumping back to an address stored on the stack. Sets the instruction pointer(IP) to another address.
 */
public class Ret implements Instruction
{
	private final VMInstruction operation = VMInstruction.RET;
	
	@Override
	public boolean match(int opcode)
	{
		int code = (opcode & 0xff000000)>>24;
		return code == operation.getCode();
	}
	
	@Override
	public void execute(VirtualMachine vm) 
	{
		Register regSP = vm.getRegister(VMRegister.REG_SP);
		int ip = vm.getMemory(regSP.getValue());
		regSP.addValue(1);
		
		Register regIP = vm.getRegister(VMRegister.REG_IP);
		regIP.setValue(ip);
	}
}
