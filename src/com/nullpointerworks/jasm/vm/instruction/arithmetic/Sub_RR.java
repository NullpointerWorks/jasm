package com.nullpointerworks.jasm.vm.instruction.arithmetic;

import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMFlag;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		SUB
 * bytes: 		12 r1 r2 ??
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		register 2
 * byte 4:		n/a
 * </pre>
 * Subtract the value of register 2 from register 1.
 */
public class Sub_RR implements Instruction
{
	private final VMInstruction operation = VMInstruction.SUB_RR;
	
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
		if (vm.hasException()) return;
		
		Register reg1 = vm.getRegister(sel1);
		Register reg2 = vm.getRegister(sel2);
		
		reg1.addValue( -reg2.getValue() );
		
		int v = reg1.getValue();
		setFlags(vm, v);
		regIP.addValue(1);
	}
	
	protected void setFlags(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.ZERO).setValue(false);
		vm.getFlag(VMFlag.SIGN).setValue(false);
		
		if (v==0) vm.getFlag(VMFlag.ZERO).setValue(true);
		if (v<0) vm.getFlag(VMFlag.SIGN).setValue(true);
	}
}
