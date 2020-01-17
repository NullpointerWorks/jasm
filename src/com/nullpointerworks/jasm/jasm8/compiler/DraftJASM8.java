package com.nullpointerworks.jasm.jasm8.compiler;

import com.nullpointerworks.jasm.jasm8.parts.InstructionsJASM8;
import com.nullpointerworks.util.StringUtil;

public class DraftJASM8 implements InstructionsJASM8
{
	private boolean hasLabel = false;
	private String label = "";
	private int code_index = 0;
	private byte[] machine_code;

	public DraftJASM8(int index, byte[] microcode)
	{
		code_index = index;
		machine_code = microcode;
	}
	
	public DraftJASM8(int index, byte[] microcode, String label)
	{
		this.label=label;
		hasLabel = true;
		code_index = index;
		machine_code = microcode;
	}
	
	
	public DraftJASM8(int index, String instruction)
	{
		code_index = index;
		String[] parts = instruction.split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		/*
		 * arithmetic
		 */
		if (instruct.equals("add")) {_alu(ADD, operands); return;}
		if (instruct.equals("sub")) {_alu(SUB, operands); return;}
		if (instruct.equals("cmp")) {_alu(CMP, operands); return;}
		if (instruct.equals("inc")) {_inc(operands); return;}
		if (instruct.equals("dec")) {_dec(operands); return;}
		if (instruct.equals("neg")) {_neg(operands); return;}
		
		
		/*
		 * other
		 */
		if (instruct.equals("sto")) {_sto(operands); return;}
		if (instruct.equals("push")) {_push(operands); return;}
		if (instruct.equals("pop")) {_pop(operands); return;}
		if (instruct.equals("ret")) {_ret(operands); return;}
	}
	
	/**
	 * push [reg8]					PUSH, Rx
	 * push [reg16]					PUSH, Rx
	 * push [imm8]					PUSH, I, (byte)0x00,
	 * push &[addr16]				PUSH, M, (byte)0x00,(byte)0x00,
	 */
	private void _push(String operands)
	{
		if ( isReg8(operands) )
		{
			byte dir = getReg8(operands);
			machine_code = new byte[] {PUSH, dir};
		}
		else
		if ( isReg16(operands) )
		{
			byte dir = getReg16(operands);
			machine_code = new byte[] {PUSH, dir};
		}
		else
		if ( isAddr16(operands) )
		{
			short addr16 = getAddr16(operands);
			byte H = (byte)(addr16>>8);
			byte L = (byte)(addr16);
			machine_code = new byte[] {PUSH, I, H, L};
		}
		else
		if ( isImm8(operands) )
		{
			byte dir = getImm8(operands);
			machine_code = new byte[] {PUSH, I, dir};
		}
		else
		{
			return;
			// error
		}
	}
	
	/**
	 * pop [reg8]					POP, Rx
	 * pop [reg16]					POP, xP
	 */
	private void _pop(String operands)
	{
		if ( isReg8(operands) )
		{
			byte dir = getReg8(operands);
			machine_code = new byte[] {POP, dir};
		}
		else
		if ( isReg16(operands) )
		{
			byte dir = getReg16(operands);
			machine_code = new byte[] {POP, dir};
		}
		else
		{
			return;
			// error
		}
	}
	
	/**
	 * ret							RET,
	 */
	private void _ret(String operands)
	{
		machine_code = new byte[] {RET};
	}
	
	/**
	 * sto &[addr16],[reg8]			STO, Rx, (byte)0x00,(byte)0x00,
	 * sto &[addr16],[imm8]			STO, I, (byte)0x00,(byte)0x00, (byte)0x01,
	 */
	private void _sto(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			return; // error
		}
		String target = tokens[0];
		if ( !isAddr16(target) )
		{
			return; // error
		}
		String source = tokens[1];
		
		short addr16 = getAddr16(target);
		byte H = (byte)(addr16>>8);
		byte L = (byte)(addr16);
		
		if ( isReg8(source) )
		{
			byte reg8 = getReg8(source);
			machine_code = new byte[] {STO, reg8, H, L};
		}
		else
		if ( isImm8(source) )
		{
			byte imm8 = getImm8(source);
			machine_code = new byte[] {STO, I, H, L, imm8};
		}
		else
		{
			return; // error
		}
	}
	
	/**
	 * neg [reg8]					NEG, Rx,
	 */
	private void _neg(String operands)
	{
		if ( isReg8(operands) )
		{
			byte dir = getReg8(operands);
			machine_code = new byte[] {NEG, dir};
		}
		else
		{
			return;
			// error
		}
	}
	
	/**
	 * opc [reg8],[reg8]			opc, Rxx,
	 * opc [reg8],[imm8]			opc, RxI, (byte)0x00,
	 */
	private void _alu(byte opcode, String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			return; // error
		}
		
		String target = tokens[0];
		
		if ( !isReg8(target) )
		{
			return; // error
		}
		
		String source = tokens[1];
		
		if ( isReg8(source) )
		{
			byte dir = getReg8Reg8(target, source);
			machine_code = new byte[] {opcode, dir};
		}
		else
		if ( isImm8(source) )
		{
			byte dir = getReg8X(target, I);
			byte imm8 = getImm8(source);
			machine_code = new byte[] {opcode, dir, imm8};
		}
		else
		{
			// error
		}
	}
	
	/**
	 * inc [reg8]					INC, Rx,
	 */
	private void _inc(String operands)
	{
		if ( isReg8(operands) )
		{
			byte dir = getReg8(operands);
			machine_code = new byte[] {INC, dir};
		}
		else
		{
			return;
			// error
		}
	}
	
	/**
	 * dec [reg8]					DEC, Rx,
	 */
	private void _dec(String operands)
	{
		if ( isReg8(operands) )
		{
			byte dir = getReg8(operands);
			machine_code = new byte[] {DEC, dir};
		}
		else
		{
			return;
			// error
		}
	}
	
	/*
	 * ===========================================================
	 */

	public final boolean hasLabel() {return hasLabel;}
	public final String getLabel() {return label;}
	public final void setLabelAddress(int addr) 
	{
		short addr16 = (short)addr;
		byte H = (byte)(addr16>>8);
		byte L = (byte)(addr16);
		machine_code[1]=H;
		machine_code[2]=L;
	}
	
	public final int codeIndex() {return code_index;}
	public final byte[] machineCode() {return machine_code;}
	public final String machineCodeToString()
	{
		String hex = "";
		for (byte b : machine_code)
		{
			hex += String.format("%02X ", b);
		}
		return hex;
	}
	
	/*
	 * ===========================================================
	 */
	
	private byte getReg8X(String reg1, byte source)
	{
		byte r1 = 0;
		switch(reg1)
		{
		case "a": r1 = RA; break;
		case "b": r1 = RB; break;
		case "c": r1 = RC; break;
		case "d": r1 = RD; break;
		case "xh": r1 = XH; break;
		case "xl": r1 = XL; break;
		case "yh": r1 = YH; break;
		case "yl": r1 = YL; break;
		}
		
		return 0; //_cd(r1,source);
	}
	
	private byte getReg8Reg8(String reg1, String reg2)
	{
		byte r1 = 0;
		byte r2 = 0;
		
		switch(reg1)
		{
		case "a": r1 = RA; break;
		case "b": r1 = RB; break;
		case "c": r1 = RC; break;
		case "d": r1 = RD; break;
		case "xh": r1 = XH; break;
		case "xl": r1 = XL; break;
		case "yh": r1 = YH; break;
		case "yl": r1 = YL; break;
		}
		
		switch(reg2)
		{
		case "a": r2 = RA; break;
		case "b": r2 = RB; break;
		case "c": r2 = RC; break;
		case "d": r2 = RD; break;
		case "xh": r2 = XH; break;
		case "xl": r2 = XL; break;
		case "yh": r2 = YH; break;
		case "yl": r2 = YL; break;
		}
		
		return 0; //InstructionsJASM8._cd(r1,r2);
	}
	
	private byte getReg8(String reg1)
	{
		byte r1 = 0;
		switch(reg1)
		{
		case "a": r1 = RA; break;
		case "b": r1 = RB; break;
		case "c": r1 = RC; break;
		case "d": r1 = RD; break;
		case "xh": r1 = XH; break;
		case "xl": r1 = XL; break;
		case "yh": r1 = YH; break;
		case "yl": r1 = YL; break;
		}
		return r1;
	}
	
	private byte getReg16(String reg1)
	{
		byte r1 = 0;
		switch(reg1)
		{
		case "ip": r1 = IP; break;
		case "sp": r1 = SP; break;
		case "x": r1 = RX; break;
		case "y": r1 = RY; break;
		}
		return r1;
	}
	
	private byte getImm8(String value)
	{
		int val = 0;
		
		if (StringUtil.isHexadec(value))
		{
			value = value.replace("0x", "");
			val = Integer.parseInt(value, 16);
		}
		else
		if (StringUtil.isInteger(value))
		{
			val = Integer.parseInt(value);
		}
		else
		{
			return -1; // error
		}
		
		byte imm8 = (byte)( val&0xff );
		return imm8;
	}
	
	private short getAddr16(String value)
	{
		value = value.replace("&", "");
		
		int val = 0;
		
		if (StringUtil.isHexadec(value))
		{
			value = value.replace("0x", "");
			val = Integer.parseInt(value, 16);
		}
		else
		if (StringUtil.isInteger(value))
		{
			val = Integer.parseInt(value);
		}
		else
		{
			return -1; // error
		}
		
		short imm16 = (short)( val&0xffff );
		return imm16;
	}
	
	/*
	 * ===========================================================
	 */
	
	private boolean isImm8(String imm)
	{
		if (StringUtil.isInteger(imm)) return true;
		if (StringUtil.isHexadec(imm)) return true;
		return false;
	}
	
	private boolean isAddr16(String addr)
	{
		if (addr.startsWith("&")) return true;
		return false;
	}
	
	private boolean isReg8(String reg)
	{
		String r = reg.toLowerCase();
		switch(r)
		{
		case "a":
		case "b":
		case "c":
		case "d":
		case "xh":
		case "xl":
		case "yh":
		case "yl": return true;
		}
		return false;
	}
	
	private boolean isReg16(String reg)
	{
		String r = reg.toLowerCase();
		switch(r)
		{
		case "ip":
		case "sp":
		case "x":
		case "y": return true;
		}
		return false;
	}
}
