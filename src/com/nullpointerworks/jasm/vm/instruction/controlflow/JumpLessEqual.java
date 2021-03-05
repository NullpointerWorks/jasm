package com.nullpointerworks.jasm.vm.instruction.controlflow;

import com.nullpointerworks.jasm.vm.Flag;
import com.nullpointerworks.jasm.vm.Instruction;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMFlag;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;
import com.nullpointerworks.jasm.vm.VirtualMachine;

/**
 * <pre>
 * instruction: 		JLE
 * bytes: 		24 ?? ?? ?? xx xx xx xx
 * byte 1:		operation code
 * byte 2:		n/a
 * byte 3:		n/a
 * byte 4:		n/a
 * byte 5:		value
 * byte 6:		value
 * byte 7:		value
 * byte 8:		value
 * </pre>
 * Jump if the sign flag is set, or if the zero flag is set. Sets the instruction pointer(IP) to another address.
 */
public class JumpLessEqual implements Instruction
{
	private final VMInstruction operation = VMInstruction.JLE;
	
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
		Flag zero = vm.getFlag(VMFlag.ZERO);
		
		boolean bool = sign.getValue() || zero.getValue();
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
