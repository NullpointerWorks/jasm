package com.nullpointerworks.jasm.vm.instruction.dataflow;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		PUSH 
 * bytes: 		37 r1 ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		n/a
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Pushes a value onto the stack
 */
public class Push_V implements Instruction
{
	private final VMInstruction operation = VMInstruction.PUSH_V;
	
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
		int value 			= vm.getMemory( regIP.getValue()+1 );
		
		Register regSP 	= vm.getRegister(VMRegister.REG_SP);
		regSP.addValue(-1);
		vm.setMemory(regSP.getValue(), value);
		
		regIP.addValue(2);
	}
}
