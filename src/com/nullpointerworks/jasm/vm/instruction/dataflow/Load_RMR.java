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
 * bytes: 		32 r1 r2 ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * </pre>
 * Load value of register into another register
 */
public class Load_RMR implements Instruction
{
	private final VMInstruction operation = VMInstruction.LOAD_RMR;
	
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
		
		int tar1 = (instruction & 0x00ff0000) >> 16;
		int tar2 = (instruction & 0x0000ff00) >> 8;
		
		VMRegister sel1 = VMRegister.findRegister(tar1);
		VMRegister sel2 = VMRegister.findRegister(tar2);
		
		if (sel1 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION, Load_RMR.class );
		if (sel2 == null) vm.throwException( VMException.VMEX_BAD_INSTRUCTION, Load_RMR.class );
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		Register reg2 = vm.getRegister(sel2);
		
		vm.setMemory(reg1.getValue(), reg2.getValue());
		regIP.addValue(1);
	}
}
