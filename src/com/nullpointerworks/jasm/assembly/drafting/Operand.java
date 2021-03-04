package com.nullpointerworks.jasm.assembly.drafting;

import com.nullpointerworks.jasm.assembly.AssemblyConstants;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.util.StringUtil;

public class Operand 
{
	private boolean hasError = true;
	private boolean isImmediate = false;
	private boolean isRegister = false;
	private boolean isAddress = false;
	private final String op;
	
	public Operand(String operand)
	{
		isAddress = checkAddress(operand);
		if (isAddress) op = operand.substring(1);
		else op = operand;
		
		isImmediate = checkImmediate(op);
		isRegister = checkRegister();
		
		if (isImmediate()) hasError = false;
		if (isRegister()) hasError = false;
	}
	
	public boolean hasError()
	{
		return hasError;
	}
	
	public boolean isImmediate()
	{
		return isImmediate;
	}
	
	public boolean isRegister()
	{
		return isRegister;
	}
	
	public boolean isAddress()
	{
		return isAddress;
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
	// ========================================================================
	
	private boolean checkAddress(String op)
	{
		if (op.startsWith( AssemblyConstants.ADDRESS )) return true;
		return false;
	}
	
	private boolean checkRegister()
	{
		return getRegister() != null;
	}
	
	private boolean checkImmediate(String op)
	{
		if (StringUtil.isInteger(op)) return true;
		if (StringUtil.isHexadec(op)) return true;
		return false;
	}
}
