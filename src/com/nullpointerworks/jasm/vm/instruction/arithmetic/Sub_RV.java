package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		SUB
 * bytes: 		13 r1 ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Subtract a value from the selected register.
 */
public class Sub_RV implements Instruction
{
	private final VMInstruction operation = VMInstruction.SUB_RV;
	
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
		
		VMRegister sel1 = VMUtil.getFirstRegister(vm, instruction);
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		reg1.addValue( -value );

		int v = reg1.getValue();
		VMUtil.checkZeroFlag(vm, v);
		VMUtil.checkSignFlag(vm, v);
		VMUtil.checkOverflowFlag(vm, v);
		regIP.addValue(2);
	}
}
