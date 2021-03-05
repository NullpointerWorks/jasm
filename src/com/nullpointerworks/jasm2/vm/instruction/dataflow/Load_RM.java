package com.nullpointerworks.jasm2.vm.instruction.dataflow;

import com.nullpointerworks.jasm2.vm.VMException;
import com.nullpointerworks.jasm2.vm.Instruction;
import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		LOAD 
 * bytes: 		32 r1 ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Load a value in memory into register
 */
public class Load_RM implements Instruction
{
	private final VMInstruction operation = VMInstruction.LOAD_RM;
	
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
		int value 			= vm.getMemory( regIP.getValue()+1 );
		
		int tar1 = (instruction & 0x00ff0000) >> 16;
		VMRegister sel1 = VMRegister.findRegister(tar1);
		if (sel1 == null)
		{
			vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
			return;
		}
		
		Register reg1 = vm.getRegister(sel1);
		reg1.setValue( vm.getMemory(value) );
		
		regIP.addValue(2);
	}
}
