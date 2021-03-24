package com.nullpointerworks.jasm.asm.translator;

import com.nullpointerworks.jasm.asm.ParserUtility;
import com.nullpointerworks.jasm.vm.VMRegister;

public class Operand 
{
	private final String op;
	private final VMRegister r;
	private final boolean isInteger;
	private final boolean isHexadec;
	private final boolean isAddress;
	private final boolean isLabel;
	private final boolean isRegister;
	
	public Operand(String o)
	{
		isAddress = ParserUtility.isAddress(o);
		if (isAddress) op = o.substring(1);
		else op = o;
		
		isInteger = ParserUtility.isInteger(op);
		isHexadec = ParserUtility.isHexadec(op);
		
		if (!isNumber())
		{
			r = findRegister(op);
			isRegister = r != null;
			if (isRegister) isLabel = false;
			else isLabel = ParserUtility.isValidLabel(op);
		}
		else
		{
			r = null;
			isRegister = false;
			isLabel = false;
		}
	}
	
	public boolean isNumber() {return isInteger || isHexadec;}
	public boolean isAddress() {return isAddress;}
	public boolean isRegister() {return isRegister;}
	public boolean isLabel() {return isLabel;}
	public String getOperand() {return op;}

	public VMRegister getRegister() {return r;}
	public int getInteger()
	{
		int val = 0;
		
		if (isHexadec)
		{
			String opp = op.replace("0x", "");
			val = Integer.parseInt(opp, 16);
		}
		else
		if (isInteger)
		{
			val = Integer.parseInt(op);
		}
		else
		{
			// error
		}
		
		return val;
	}
	
	private VMRegister findRegister(String name)
	{
		String r = op.toLowerCase();
		switch(r)
		{
		case "ip": return VMRegister.REG_IP;
		case "sp": return VMRegister.REG_SP;
		
		case "a": return VMRegister.REG_A;
		case "b": return VMRegister.REG_B;
		case "c": return VMRegister.REG_C;
		case "d": return VMRegister.REG_D;

		case "r0": return VMRegister.REG_0;
		case "r1": return VMRegister.REG_1;
		case "r2": return VMRegister.REG_2;
		case "r3": return VMRegister.REG_3;
		case "r4": return VMRegister.REG_4;
		case "r5": return VMRegister.REG_5;
		case "r6": return VMRegister.REG_6;
		case "r7": return VMRegister.REG_7;
		case "r8": return VMRegister.REG_8;
		case "r9": return VMRegister.REG_9;
		default: break;
		}
		return null;
	}
}
