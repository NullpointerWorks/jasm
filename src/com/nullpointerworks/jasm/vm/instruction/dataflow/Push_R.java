package com.nullpointerworks.jasm.vm.instruction.dataflow;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMException;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		PUSH 
 * bytes: 		34 r1 ?? ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * </pre>
 * Pushes the value of a register onto the stack
 */
public class Push_R implements Instruction
{
	private final VMInstruction operation = VMInstruction.PUSH_R;
	
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
		int tar1 			= (instruction & 0x00ff0000) >> 16;
		
		VMRegister sel1 = VMRegister.findRegister(tar1);
		if (sel1 == null)
		{
			vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
			return;
		}
		Register reg1 = vm.getRegister(sel1);
		
		Register regSP 	= vm.getRegister(VMRegister.REG_SP);
		regSP.addValue(-1);
		vm.setMemory(regSP.getValue(), reg1.getValue());
		
		regIP.addValue(1);
	}
}
