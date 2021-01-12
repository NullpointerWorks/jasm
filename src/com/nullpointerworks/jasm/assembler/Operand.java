package com.nullpointerworks.jasm.assembler;

import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.util.StringUtil;

public class Operand 
{
	private boolean hasError = true;
	private final String op;
	
	public Operand(String operand)
	{
		op = operand;
		if (isImmediate()) hasError = false;
		if (isRegister()) hasError = false;
	}
	
	public boolean hasError()
	{
		return hasError;
	}
	
	public String getBytecode()
	{
		if (isImmediate())
		{
			int imm = getImmediate();
			
			// MSB first
			int byte1 = (imm>>24)&0xff;
			int byte2 = (imm>>16)&0xff;
			int byte3 = (imm>>8)&0xff;
			int byte4 = (imm)&0xff;

			String hex1 = Integer.toHexString(byte1);
			String hex2 = Integer.toHexString(byte2);
			String hex3 = Integer.toHexString(byte3);
			String hex4 = Integer.toHexString(byte4);
			
			hex1 = ("00" + hex1).substring(hex1.length());
			hex2 = ("00" + hex2).substring(hex2.length());
			hex3 = ("00" + hex3).substring(hex3.length());
			hex4 = ("00" + hex4).substring(hex4.length());
			
			return (hex1+" "+hex2+" "+hex3+" "+hex4).toUpperCase();
		}
		
		if (isRegister())
		{
			int reg = getRegister().ordinal();
			String hex = Integer.toHexString(reg);
			return hex.toUpperCase();
		}
		
		return "00";
	}
	
	public boolean isImmediate()
	{
		if (StringUtil.isInteger(op)) return true;
		if (StringUtil.isHexadec(op)) return true;
		return false;
	}
	
	public int getImmediate()
	{
		int val = 0;
		
		if (StringUtil.isHexadec(op))
		{
			String opp = op.replace("0x", "");
			val = Integer.parseInt(opp, 16);
		}
		else
		if (StringUtil.isInteger(op))
		{
			val = Integer.parseInt(op);
		}
		else
		{
			// error
		}
		
		return val;
	}
	
	public boolean isRegister()
	{
		return getRegister() != null;
	}
	
	public Select getRegister()
	{
		String r = op.toLowerCase();
		switch(r)
		{
		case "ip": return Select.IP;
		case "sp": return Select.SP;
		
		case "a": return Select.REG_A;
		case "b": return Select.REG_B;
		case "c": return Select.REG_C;
		case "d": return Select.REG_D;
		
		case "i": return Select.REG_I;
		case "j": return Select.REG_J;
		case "k": return Select.REG_K;
		
		case "u": return Select.REG_U;
		case "v": return Select.REG_V;
		case "w": return Select.REG_W;
		
		case "x": return Select.REG_X;
		case "y": return Select.REG_Y;
		case "z": return Select.REG_Z;
		default: break;
		}
		return null; // error
	}
}
