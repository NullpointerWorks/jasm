package com.nullpointerworks.jasm.jasm8.compiler;

import com.nullpointerworks.jasm.jasm8.processor.InstructionsJASM8;
import com.nullpointerworks.util.StringUtil;

public class DraftBuilder 
implements InstructionsJASM8
{
	private static DraftBuilder inst = null;
	public static DraftBuilder getInstance()
	{
		if (inst==null) inst = new DraftBuilder();
		return inst;
	}
	private DraftBuilder() { }

	public static Draft getDraft(String instruction)
	{
		instruction = instruction.toLowerCase();
		String[] parts = instruction.split(" ");
		String instruct = parts[0];
		String operands = "";
		if (parts.length > 1) operands = parts[1];
		DraftBuilder inst = getInstance();
		
		/*
		 * generic
		 */
		if (instruct.equals("nop")) return inst.comm(operands, NOP);
		if (instruct.equals("end")) return inst.comm(operands, END);
		if (instruct.equals("ret")) return inst.comm(operands, RET);
		if (instruct.equals("out")) return inst.out(operands);
		
		/*
		 * memory operations
		 */
		if (instruct.equals("load")) return inst.load(operands);
		if (instruct.equals("sto")) return inst.sto(operands);
		if (instruct.equals("rd")) return inst.rd(operands);
		if (instruct.equals("push")) return inst.push(operands);
		if (instruct.equals("pop")) return inst.latch(operands, POP);
		
		/*
		 * ALU operations
		 */
		if (instruct.equals("add")) return inst.alu(operands, ADD);
		if (instruct.equals("sub")) return inst.alu(operands, SUB);
		if (instruct.equals("cmp")) return inst.alu(operands, CMP);
		if (instruct.equals("neg")) return inst.latch(operands, NEG);
		if (instruct.equals("inc")) return inst.latch(operands, INC);
		if (instruct.equals("dec")) return inst.latch(operands, DEC);
		if (instruct.equals("shl")) return inst.latch(operands, SHL);
		if (instruct.equals("shr")) return inst.latch(operands, SHR);
		if (instruct.equals("bit")) return inst.bit(operands);
		
		/*
		 * label jumps
		 */
		if (instruct.equals("call")) return inst.jmp(operands, CALL);
		if (instruct.equals("jmp")) return inst.jmp(operands, JMP);
		if (instruct.equals("jz")) return inst.jmp(operands, JE); // duplicate for convenience
		if (instruct.equals("je")) return inst.jmp(operands, JE);
		if (instruct.equals("jnz")) return inst.jmp(operands, JNE); // duplicate for convenience
		if (instruct.equals("jne")) return inst.jmp(operands, JNE);
		if (instruct.equals("jl")) return inst.jmp(operands, JL);
		if (instruct.equals("jle")) return inst.jmp(operands, JLE);
		if (instruct.equals("jg")) return inst.jmp(operands, JG);
		if (instruct.equals("jge")) return inst.jmp(operands, JGE);
		
		return null;
	}
	
	// ==================================================================
	// BIT
	// ==================================================================
	
	private Draft bit(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) return null; // error
		String target = tokens[0];
		String source = tokens[1];
		
		if ( isRegister(target) )
		{
			byte reg = getRegister(target);
			if ( isImm8(source) )
			{
				byte dir = getImm8(source);
				byte[] mc = new byte[] {BIT, reg, dir};
				return new Draft(mc);
			}
		}
		
		return null; // generic error
	}
	
	// ==================================================================
	// PUSH
	// ==================================================================
	
	private Draft push(String operands)
	{
		if ( isRegister(operands) )
		{
			return latch(operands, PUSH);
		}
		
		if ( isImm8(operands) )
		{
			byte dir = getImm8(operands);
			byte[] mc = new byte[] {PUSH, I, dir};
			return new Draft(mc);
		}
		
		if ( isImm16(operands) )
		{
			short imm16 = getImm16(operands);
			byte H = (byte)(imm16>>8);
			byte L = (byte)(imm16);
			byte[] mc = new byte[] {PUSH, IL, H, L};
			return new Draft(mc);
		}
		
		return null; // generic error
	}
	
	// ==================================================================
	// RD
	// ==================================================================
	
	private Draft rd(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) return null; // error
		String target = tokens[0];
		String source = tokens[1];
		
		if ( isReg8(target) )
		{
			byte reg8 = getRegister(target);
			if ( isAddress(source) )
			{
				source = source.substring(1);
				byte dir = compileDirective(reg8,IL);
				short addr16 = getImm16(source);
				byte H = (byte)(addr16>>8);
				byte L = (byte)(addr16);
				byte[] mc = new byte[] {MRD, dir, H, L};
				return new Draft(mc);
			}
			
			if ( isRegister(source) )
			{
				byte reg = getRegister(source);
				byte dir = compileDirective(reg8,reg);
				byte[] mc = new byte[] {MRD, dir};
				return new Draft(mc);
			}
		}
		
		return null; // generic error
	}
	
	// ==================================================================
	// STO
	// ==================================================================
	
	private Draft sto(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) return null; // error
		String target = tokens[0];
		String source = tokens[1];
		
		if ( isAddress(target) )
		{
			target = target.substring(1);
			byte[] machine_code = null;
			short addr16 = getImm16(target);
			byte H = (byte)(addr16>>8);
			byte L = (byte)(addr16);
			
			if ( isReg8(source) )
			{
				byte reg8 = getRegister(source);
				machine_code = new byte[] {STO, reg8, H, L};
				return new Draft(machine_code);
			}
			
			if ( isImm8(source) )
			{
				byte imm8 = getImm8(source);
				machine_code = new byte[] {STO, I, H, L, imm8};
				return new Draft(machine_code);
			}
		}
		
		if ( isReg16(target) )
		{
			byte regH = getRegister(target);
			
			if ( isReg8(source) )
			{
				byte regL = getRegister(source);
				byte dir = compileDirective(regH,regL);
				byte[] machine_code = new byte[] {STO, dir};
				return new Draft(machine_code);
			}
			
			if ( isImm8(source) )
			{
				byte imm8 = getImm8(source);
				byte dir = compileDirective(regH,I);
				byte[] machine_code = new byte[] {STO, dir, imm8};
				return new Draft(machine_code);
			}
		}
		
		return null; // generic error
	}
	
	// ==================================================================
	// NEG INC DEC POP SHL SHR
	// ==================================================================
	
	private Draft latch(String operands, byte opcode)
	{
		if ( isRegister(operands) )
		{
			byte reg = getRegister(operands);
			byte[] mc = new byte[] {opcode, reg};
			return new Draft(mc);
		}
		return null;
	}
	
	// ==================================================================
	// ADD SUB CMP
	// ==================================================================
	
	private Draft alu(String operands, byte opcode)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) return null; // error
		String target = tokens[0];
		String source = tokens[1];
		byte[] machine_code = null;
		
		// any register
		if ( isRegister(target) )
		{
			byte reg = getRegister(target);
			
			if ( isReg8(source) )
			{
				byte rS = getRegister(source);
				byte dir = compileDirective(reg,rS);
				machine_code = new byte[] {opcode, dir};
				return new Draft(machine_code);
			}
			
			if ( isImm8(source) )
			{
				byte val = getImm8(source);
				byte dir = compileDirective(reg,I);
				machine_code = new byte[] {opcode, dir, val};
				return new Draft(machine_code);
			}
		}
		
		// register 8-bit
		if ( isReg8(target) )
		{
			if ( isReg16(source) )
			{
				// bad register loading error
			}
			
			if ( isImm16(source) )
			{
				// bad value loading error
			}
		}
		
		// register 16-bit
		if ( isReg16(target) )
		{
			byte reg = getRegister(target);
			
			if ( isReg16(source) )
			{
				byte rS = getRegister(source);
				byte dir = compileDirective(reg,rS);
				machine_code = new byte[] {opcode, dir};
				return new Draft(machine_code);
			}
			
			if ( isImm16(source) )
			{
				byte dir = compileDirective(reg,IL);
				short i16 = getImm16(source);
				byte H = (byte)(i16>>8);
				byte L = (byte)(i16);
				machine_code = new byte[] {opcode, dir, H, L};
				return new Draft(machine_code);
			}
		}
		
		return null; // generic error
	}
	
	// ==================================================================
	// LOAD
	// ==================================================================
	
	private Draft load(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) return null; // error
		String target = tokens[0];
		String source = tokens[1];
		byte[] machine_code = null;
		
		// any register
		if ( isRegister(target) )
		{
			byte reg = getRegister(target);
			
			if ( isReg8(source) )
			{
				byte rS = getRegister(source);
				byte dir = compileDirective(reg,rS);
				machine_code = new byte[] {LOAD, dir};
				return new Draft(machine_code);
			}
			
			if ( isImm8(source) )
			{
				byte dir = compileDirective(reg,I);
				byte val = getImm8(source);
				machine_code = new byte[] {LOAD, dir, val};
				return new Draft(machine_code);
			}
			
			/* 
			
			removed - load can no longer read directly from ram (as of 1.2.0 beta)
			
			if ( isAddress(source) )
			{
				source = source.substring(1);
				byte dir = compileDirective(reg,M);
				short addr16 = getImm16(source);
				byte H = (byte)(addr16>>8);
				byte L = (byte)(addr16);
				machine_code = new byte[] {LOAD, dir, H, L};
				return new Draft(machine_code);
			}
			//*/
		}
		
		// register 8-bit
		if ( isReg8(target) )
		{
			if ( isReg16(source) )
			{
				// bad register loading error
			}
			
			if ( isImm16(source) )
			{
				// bad value loading error
			}
		}
		
		// register 16-bit
		if ( isReg16(target) )
		{
			byte reg = getRegister(target);
			
			if ( isReg16(source) )
			{
				byte rS = getRegister(source);
				byte dir = compileDirective(reg,rS);
				machine_code = new byte[] {LOAD, dir};
				return new Draft(machine_code);
			}
			
			if ( isImm16(source) )
			{
				byte dir = compileDirective(reg,IL);
				short i16 = getImm16(source);
				byte H = (byte)(i16>>8);
				byte L = (byte)(i16);
				machine_code = new byte[] {LOAD, dir, H, L};
				return new Draft(machine_code);
			}
		}
		
		return null; // generic error
	}
	
	// ==================================================================
	// OUT
	// ==================================================================
	
	private Draft out(String op)
	{
		byte[] machine_code = null;
		
		if ( isRegister(op) )
		{
			byte reg = getRegister(op);
			machine_code = new byte[] {OUT, reg};
			return new Draft(machine_code);
		}
		
		if ( isImm8(op) )
		{
			byte imm8 = getImm8(op);
			machine_code = new byte[] {OUT, I, imm8};
			return new Draft(machine_code);
		}
		
		if ( isImm16(op) )
		{
			short imm16 = getImm16(op);
			byte H = (byte)(imm16>>8);
			byte L = (byte)(imm16);
			machine_code = new byte[] {OUT, IL, H, L};
			return new Draft(machine_code);
		}
		
		return null;// error
	}
	
	// ==================================================================
	// JUMP - JMP, JE, JNE, JL, JLE, JG, JGE
	// ==================================================================
	
	private Draft jmp(String operands, byte opcode)
	{
		byte[] mc = new byte[] {opcode, 0, 0};
		return new Draft(mc,operands);
	}
	
	// ==================================================================
	// NOP END RET
	// ==================================================================
	
	private Draft comm(String op, byte opcode)
	{
		byte[] machine_code = new byte[] {opcode};
		return new Draft(machine_code);
	}
	
	/*
	 * ===========================================================
	 */
	
	private byte compileDirective(byte a, byte b)
	{
		a = (byte)(a<<4);
		a = (byte)(a&0b1111_0000);
		return (byte)(a|b);
	}
	
	private byte getRegister(String reg)
	{
		switch(reg)
		{
		case "a": return RA;
		case "b": return RB;
		case "c": return RC;
		case "d": return RD;
		case "xh": return XH;
		case "xl": return XL;
		case "yh": return YH;
		case "yl": return YL;
		case "ip": return IP;
		case "sp": return SP;
		case "x": return RX;
		case "y": return RY;
		}
		return -1;
	}

	private byte getImm8(String value)
	{
		int val = _get_value(value);
		byte imm8 = (byte)( val&0xff );
		return imm8;
	}
	
	private short getImm16(String value)
	{
		int val = _get_value(value);
		short imm16 = (short)( val&0xffff );
		return imm16;
	}
	
	private int _get_value(String value)
	{
		if (StringUtil.isHexadec(value))
		{
			value = value.substring(2);
			return Integer.parseInt(value, 16);
		}
		else
		if (StringUtil.isInteger(value))
		{
			return Integer.parseInt(value);
		}
		else
		{
			return -1; // error
		}
	}
	
	/*
	 * ===========================================================
	 */
	
	private boolean isRegister(String reg)
	{
		switch(reg)
		{
		case "a":
		case "b":
		case "c":
		case "d":
		case "xh":
		case "xl":
		case "yh":
		case "yl":
		case "ip":
		case "sp":
		case "x":
		case "y": return true;
		}
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
	
	private boolean isImm8(String imm)
	{
		if (StringUtil.isInteger(imm) || StringUtil.isHexadec(imm)) 
		{
			short v = getImm16(imm);
			if (v < 256) return true;
		}
		return false;
	}
	
	private boolean isImm16(String imm)
	{
		if (StringUtil.isInteger(imm) || StringUtil.isHexadec(imm))
		{
			return true;
		}
		return false;
	}
	
	private boolean isAddress(String addr)
	{
		return addr.startsWith("&");
	}
}
