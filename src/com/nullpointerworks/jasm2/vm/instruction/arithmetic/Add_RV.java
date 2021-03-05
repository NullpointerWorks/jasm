package com.nullpointerworks.jasm2.vm.instruction.arithmetic;

import com.nullpointerworks.jasm2.vm.VMFlag;
import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.VirtualMachine;
import com.nullpointerworks.jasm2.vm.Instruction;

/**
 * <pre>
 * instruction: 		ADD
 * bytes: 		11 r1 ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		register 1
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Load value into register
 */
public class Add_RV implements Instruction
{
	private final VMInstruction operation = VMInstruction.ADD_RV;
	
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
		reg1.addValue( value );
		
		int v = reg1.getValue();
		setFlags(vm, v);
		regIP.addValue(2);
	}
	
	protected void setFlags(VirtualMachine vm, int v) 
	{
		vm.getFlag(VMFlag.ZERO).setValue(false);
		vm.getFlag(VMFlag.SIGN).setValue(false);
		
		if (v==0) vm.getFlag(VMFlag.ZERO).setValue(true);
		if (v<0) vm.getFlag(VMFlag.SIGN).setValue(true);
	}
}
