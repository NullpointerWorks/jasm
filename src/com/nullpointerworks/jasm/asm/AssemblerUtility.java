package com.nullpointerworks.jasm.asm;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;

public class AssemblerUtility 
{
	public static void setCodeImmidiate(Draft d, VMInstruction inst, int value)
	{
		int opcode = inst.getCode() << 24;
		int imm = value & 0x00ffffff;
		int o = opcode | imm;
		d.addValue(o);
	}
	
	public static void setCode(Draft d, VMInstruction inst)
	{
		int opcode = inst.getCode() << 24;
		d.addValue(opcode);
	}
	
	public static void setCode(Draft d, VMInstruction inst, int value)
	{
		int opcode = inst.getCode() << 24;
		d.addValue(opcode);
		d.addValue(value);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int o = opcode | r1;
		d.addValue(o);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1, int value)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int o = opcode | r1;
		d.addValue(o);
		d.addValue(value);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1, VMRegister reg2)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int r2 = reg2.ordinal() << 8;
		int o = opcode | r1 | r2;
		d.addValue(o);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1, VMRegister reg2, VMRegister reg3)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int r2 = reg2.ordinal() << 8;
		int r3 = reg3.ordinal();
		int o = opcode | r1 | r2 | r3;
		d.addValue(o);
	}
}
