package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.List;

public final class BytecodeBuilder
{
	private List<Draft> draft;
	private List<Bytecode> instructions;
	
	public BytecodeBuilder()
	{
		instructions = new ArrayList<Bytecode>();
	}
	
	public void setDraft(List<Draft> draft)
	{
		this.draft = draft;
	}
	
	public List<Bytecode> getBytecode()
	{
		return instructions;
	}
	
	public void convert()
	{
		instructions.clear();
		for (Draft d : draft)
		{
			Operation opcode = d.getOperation();
			
			switch(opcode)
			{
			case LOAD:
				_load(d);
				break;
			case READ:
				_read(d);
				break;
			case STO:
				_store(d);
				break;
			case PUSH:
				_push(d);
				break;
			case POP:
				_pop(d);
				break;
				
				
			case JMP:
				_jump("30", d.getJumpAddress());
				break;
			case CALL:
				_jump("37", d.getJumpAddress());
				break;
			case RET:
				_jump("38", d.getJumpAddress());
				break;
			case JE:
				_jump("31", d.getJumpAddress());
				break;
			case JNE:
				_jump("32", d.getJumpAddress());
				break;
			case JL:
				_jump("35", d.getJumpAddress());
				break;
			case JLE:
				_jump("36", d.getJumpAddress());
				break;
			case JG:
				_jump("33", d.getJumpAddress());
				break;
			case JGE:
				_jump("34", d.getJumpAddress());
				break;
				
				
			case ADD: 
				_addition(d);
				break;
				
			case SUB: 
				_subtract(d);
				break;
				
			case CMP: 
				_compare(d);
				break;
				
			case INC: 
				_increment(d);
				break;
				
			case DEC: 
				_decrement(d);
				break;
				
			case NEG: 
				_negate(d);
				break;
				
			case SHL: 
				_shiftleft(d);
				break;
				
			case SHR: 
				_shiftright(d);
				break;
				
				
			case INT:
				_interrupt(d);
				break;
			
			case NOP: 
			default:
				_default();
				break;
			}
		}
	}

	/*
	 * ======================================
	 */
	
	private void _jump(String opcode, int imm) 
	{
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
		
		String bc = (hex1+" "+hex2+" "+hex3+" "+hex4).toUpperCase();
		
		instructions.add( new Bytecode(opcode+" "+bc) );
	}
	
	private void _read(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("47 "+bc1+bc2 ) );
		}
		else
		if (op2.isImmediate())
		{
			String bc1 = op1.getBytecode();
			bc1 = ("00" + bc1).substring(bc1.length());
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("48 "+bc1+" "+bc2 ) );
		}
	}
	
	private void _store(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op1.isRegister())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("45 "+bc1+bc2 ) );
		}
		else
		if (op1.isImmediate())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			bc2 = ("00" + bc2).substring(bc2.length());
			instructions.add( new Bytecode("46 "+bc2+" "+bc1 ) );
		}
	}
	
	private void _pop(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		
		if (op.isRegister())
		{
			String bc = op.getBytecode();
			bc = ("00" + bc).substring(bc.length());
			instructions.add( new Bytecode("44 "+bc ) );
		}
	}
	
	private void _push(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		
		if (op.isRegister())
		{
			String bc = op.getBytecode();
			bc = ("00" + bc).substring(bc.length());
			instructions.add( new Bytecode("42 "+bc ) );
		}
		else
		if (op.isImmediate())
		{
			String bc = op.getBytecode();
			instructions.add( new Bytecode("43 "+bc ) );
		}
	}

	private void _load(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("40 "+bc1+bc2 ) );
		}
		else
		if (op2.isImmediate())
		{
			String bc1 = op1.getBytecode();
			bc1 = ("00" + bc1).substring(bc1.length());
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("41 "+bc1+" "+bc2 ) );
		}
	}
	
	private void _decrement(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		String bc = op.getBytecode();
		bc = ("00" + bc).substring(bc.length());
		instructions.add( new Bytecode("24 "+bc ) );
	}
	
	private void _increment(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		String bc = op.getBytecode();
		bc = ("00" + bc).substring(bc.length());
		instructions.add( new Bytecode("23 "+bc ) );
	}
	
	private void _negate(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		String bc = op.getBytecode();
		bc = ("00" + bc).substring(bc.length());
		instructions.add( new Bytecode("22 "+bc ) );
	}
	
	private void _shiftright(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		String bc = op.getBytecode();
		bc = ("00" + bc).substring(bc.length());
		instructions.add( new Bytecode("21 "+bc ) );
	}
	
	private void _shiftleft(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		String bc = op.getBytecode();
		bc = ("00" + bc).substring(bc.length());
		instructions.add( new Bytecode("20 "+bc ) );
	}
	
	private void _compare(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("14 "+bc1+bc2 ) );
		}
		else
		if (op2.isImmediate())
		{
			String bc1 = op1.getBytecode();
			bc1 = ("00" + bc1).substring(bc1.length());
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("15 "+bc1+" "+bc2 ) );
		}
	}
	
	private void _subtract(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("12 "+bc1+bc2 ) );
		}
		else
		if (op2.isImmediate())
		{
			String bc1 = op1.getBytecode();
			bc1 = ("00" + bc1).substring(bc1.length());
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("13 "+bc1+" "+bc2 ) );
		}
	}

	private void _addition(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			String bc1 = op1.getBytecode();
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("10 "+bc1+bc2 ) );
		}
		else
		if (op2.isImmediate())
		{
			String bc1 = op1.getBytecode();
			bc1 = ("00" + bc1).substring(bc1.length());
			String bc2 = op2.getBytecode();
			instructions.add( new Bytecode("11 "+bc1+" "+bc2 ) );
		}
	}
	
	private void _interrupt(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new Bytecode("01 "+op.getBytecode() ) );
	}

	private void _default()
	{
		instructions.add( new Bytecode("00") );
	}
}
