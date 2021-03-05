package com.nullpointerworks.jasm2.vm.instruction.arithmetic;

import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.VirtualMachine;
import com.nullpointerworks.jasm2.vm.Instruction;

/**
 * <pre>
 * instruction: 		DIV
 * bytes: 		1b r1 r2 r3
 * byte 1:		operation code
 * byte 2:		register 1 - numerator
 * byte 3:		register 2 - denominator
 * byte 4:		register 3 - remainder
 * </pre>
 * Dvivide the content of two registers. Stores the result in register 1, and the remainder in register 3.
 */
public class Div_RR implements Instruction
{
	private final VMInstruction operation = VMInstruction.DIV_RR;
	
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
		
		VMRegister sel1 = VMUtil.getFirstRegister(vm, instruction);
		VMRegister sel2 = VMUtil.getSecondRegister(vm, instruction);
		VMRegister sel3 = VMUtil.getThirdRegister(vm, instruction);
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		Register reg2 = vm.getRegister(sel2);
		Register reg3 = vm.getRegister(sel3);
		
		int result = reg1.getValue() / reg2.getValue();
		int remain = reg1.getValue() % reg2.getValue();
		
		reg3.setValue(remain);
		reg1.setValue(result);
		VMUtil.setFlags(vm, result);
		regIP.addValue(1);
	}
}
