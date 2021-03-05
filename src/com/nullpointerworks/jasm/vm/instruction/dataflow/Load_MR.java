package com.nullpointerworks.jasm.vm.instruction.dataflow;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMException;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		LOAD 
 * bytes: 		35 r1 ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Load a value in a register into memory
 */
public class Load_MR implements Instruction
{
	private final VMInstruction operation = VMInstruction.LOAD_MR;
	
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
		int index 			= vm.getMemory( regIP.getValue()+1 );
		
		int tar1 = (instruction & 0x00ff0000) >> 16;
		VMRegister sel = VMRegister.findRegister(tar1);
		if (sel == null)
		{
			vm.throwException( VMException.VMEX_BAD_INSTRUCTION );
			return;
		}
		
		Register reg = vm.getRegister(sel);
		int value = reg.getValue();
		vm.setMemory(index, value);
		regIP.addValue(2);
	}
}
