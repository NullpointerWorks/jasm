package com.nullpointerworks.jasm.asm.assembler.builder;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.vm.VMInstruction;
import com.nullpointerworks.jasm.vm.VMRegister;

class BuilderUtil 
{
	private static final String INTEGER = "[+-]?\\d+";
	private static final String HEXADEC = "^(0x|0X|#).[0-9A-Fa-f]+";
	
	public static boolean isInteger(String str)
	{
		return str.matches(INTEGER);
	}
	
	public static boolean isHexadec(String str)
	{
		return str.matches(HEXADEC);
	}
	
	public static void setCodeImmidiate(Draft d, VMInstruction inst, int value)
	{
		int opcode = inst.getCode() << 24;
		int imm = value & 0x00ffffff;
		int o = opcode | imm;
		d.addMachineCode(o);
	}
	
	public static void setCode(Draft d, VMInstruction inst)
	{
		int opcode = inst.getCode() << 24;
		d.addMachineCode(opcode);
	}
	
	public static void setCode(Draft d, VMInstruction inst, int value)
	{
		int opcode = inst.getCode() << 24;
		d.addMachineCode(opcode);
		d.addMachineCode(value);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int o = opcode | r1;
		d.addMachineCode(o);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1, int value)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int o = opcode | r1;
		d.addMachineCode(o);
		d.addMachineCode(value);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1, VMRegister reg2)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int r2 = reg2.ordinal() << 8;
		int o = opcode | r1 | r2;
		d.addMachineCode(o);
	}
	
	public static void setCode(Draft d, VMInstruction inst, VMRegister reg1, VMRegister reg2, VMRegister reg3)
	{
		int opcode = inst.getCode() << 24;
		int r1 = reg1.ordinal() << 16;
		int r2 = reg2.ordinal() << 8;
		int r3 = reg3.ordinal();
		int o = opcode | r1 | r2 | r3;
		d.addMachineCode(o);
	}
}
