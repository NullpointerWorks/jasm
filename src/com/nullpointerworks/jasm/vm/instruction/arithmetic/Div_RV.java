package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		DIV
 * bytes: 		1c r1 r2 ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Dvivide the content of register 1 with a given value. Stores the result in register 1, and the remainder in register 2.
 */
public class Div_RV implements Instruction
{
	private final VMInstruction operation = VMInstruction.DIV_RV;
	
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
		VMRegister sel2 = VMUtil.getSecondRegister(vm, instruction);
		if (vm.hasException()) return;

		Register reg1 = vm.getRegister(sel1);
		Register reg2 = vm.getRegister(sel2);
		
		int result = reg1.getValue() / value;
		int remain = reg1.getValue() % value;
		
		reg2.setValue(remain);
		reg1.setValue(result);
		VMUtil.setFlags(vm, result);
		regIP.addValue(2);
	}
}
