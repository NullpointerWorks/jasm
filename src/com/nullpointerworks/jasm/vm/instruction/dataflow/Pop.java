package com.nullpointerworks.jasm.vm.instruction.dataflow;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMException;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		POP 
 * bytes: 		38 r1 ?? ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * </pre>
 * Pops a value from the stack into a register
 */
public class Pop implements Instruction
{
	private final VMInstruction operation = VMInstruction.POP;
	
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
			vm.throwException( VMException.VMEX_BAD_INSTRUCTION, Pop.class );
			return;
		}
		Register reg1 = vm.getRegister(sel1);
		
		Register regSP = vm.getRegister(VMRegister.REG_SP);
		int value = vm.getMemory(regSP.getValue());
		reg1.setValue(value);
		
		regSP.addValue(1);
		regIP.addValue(1);
	}
}
