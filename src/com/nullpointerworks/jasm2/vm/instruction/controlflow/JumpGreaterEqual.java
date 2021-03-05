package com.nullpointerworks.jasm2.vm.instruction.controlflow;

import com.nullpointerworks.jasm2.vm.Instruction;
import com.nullpointerworks.jasm2.vm.VMInstruction;
import com.nullpointerworks.jasm2.vm.Register;
import com.nullpointerworks.jasm2.vm.VMFlag;
import com.nullpointerworks.jasm2.vm.VMRegister;
import com.nullpointerworks.jasm2.vm.Flag;
import com.nullpointerworks.jasm2.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		JGE
 * bytes: 		26 ?? ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		n/a
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Jump if the sign flag is not set. Sets the instruction pointer(IP) to another address.
 */
public class JumpGreaterEqual implements Instruction
{
	private final VMInstruction operation = VMInstruction.JGE;
	
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
		Flag sign = vm.getFlag(VMFlag.SIGN);
		
		boolean bool = !sign.getValue();
		
		if (bool)
		{
			int value = vm.getMemory( regIP.getValue()+1 );
			regIP.setValue(value);
		}
		else
		{
			regIP.addValue(2);
		}
	}
}
