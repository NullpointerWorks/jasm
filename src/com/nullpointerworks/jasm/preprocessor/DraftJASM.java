package com.nullpointerworks.jasm.preprocessor;

import com.nullpointerworks.jasm.instruction.Instruction;
import com.nullpointerworks.jasm.instruction.InstructionFactory;
import com.nullpointerworks.jasm.instruction.InstructionSet;

import com.nullpointerworks.jasm.parser.SourceCode;
import com.nullpointerworks.jasm.processor.Select;
import com.nullpointerworks.util.StringUtil;

public class DraftJASM
{
	private DraftError error_flag = DraftError.NO_ERROR;
	private SourceCode loc = null;
	
	private String label = "";
	private int code_index = 0;
	private Instruction draft = null;
	
	public DraftJASM(int index, SourceCode loc)
	{
		this.loc=loc;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		draft(index,instruct,operands);
	}
	
	private void draft(int index, String instruct, String operands)
	{
		code_index = index;

		if (instruct.equals("nop")) {_nop(); return;}
		if (instruct.equals("int")) {_int(operands); return;}
		
		if (instruct.equals("add")) {_generic(InstructionSet.ADD, operands); return;}
		if (instruct.equals("sub")) {_generic(InstructionSet.SUB, operands); return;}
		if (instruct.equals("cmp")) {_generic(InstructionSet.CMP, operands); return;}
		
		if (instruct.equals("load")) {_generic(InstructionSet.LOAD, operands); return;}
		
		
		if (instruct.equals("jmp")) {_jump(InstructionSet.JMP, operands); return;}
		if (instruct.equals("je")) {_jump(InstructionSet.JE, operands); return;}
		if (instruct.equals("jne")) {_jump(InstructionSet.JNE, operands); return;}
		if (instruct.equals("jl")) {_jump(InstructionSet.JL, operands); return;}
		if (instruct.equals("jle")) {_jump(InstructionSet.JLE, operands); return;}
		if (instruct.equals("jg")) {_jump(InstructionSet.JG, operands); return;}
		if (instruct.equals("jge")) {_jump(InstructionSet.JGE, operands); return;}
		
		/*
		// arithmetic
		if (instruct.equals("inc")) {_inc(operands); return;}
		if (instruct.equals("dec")) {_dec(operands); return;}
		if (instruct.equals("neg")) {_neg(operands); return;}
		
		// jump
		if (instruct.equals("call")) {_jmp(CALL,operands); return;}
		
		// other
		if (instruct.equals("end")) {_end(); return;}
		if (instruct.equals("load")) {_load(operands); return;}
		if (instruct.equals("sto")) {_sto(operands); return;}
		if (instruct.equals("push")) {_push(operands); return;}
		if (instruct.equals("pop")) {_pop(operands); return;}
		if (instruct.equals("ret")) {_ret(operands); return;}
		//*/
	}
	
	/*
	 * ===========================================================
	 */

	private boolean isImmediate(String imm)
	{
		if (StringUtil.isInteger(imm)) return true;
		if (StringUtil.isHexadec(imm)) return true;
		return false;
	}
	
	private int getImmediate(String value)
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
			// error
		}
		
		return val;
	}
	
	private boolean isRegister(String reg)
	{
		return getRegister(reg) != null;
	}
	
	private Select getRegister(String reg)
	{
		String r = reg.toLowerCase();
		switch(r)
		{
		case "ip": return Select.IP;
		case "sp": return Select.SP;
		case "a": return Select.REG_A;
		case "b": return Select.REG_B;
		case "c": return Select.REG_C;
		case "d": return Select.REG_D;
		default: break;
		}
		return null; // error
	}
	
	private boolean isAddress(String addr)
	{
		if (addr.startsWith("&")) return true;
		return false;
	}
	
	private int getAddress(String value)
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
			// error
		}
		
		return val;
	}
	
	/*
	 * ===========================================================
	 */
	
	public SourceCode getLineOfCode() {return loc;}
	
	public Instruction getInstruction() {return draft;}
	
	public final boolean hasLabel() {return label.length() > 0;}
	
	public final String getLabel() {return label;}
	
	public final void setLabelAddress(int addr) {draft.setAddress(addr);}
	
	public final int getCodeIndex() {return code_index;}
	
	public boolean hasError() {return error_flag != DraftError.NO_ERROR;}
	
	public DraftError getError() {return error_flag;}
	
	/*
	 * ===========================================================
	 */
	private void _nop()
	{
		draft = InstructionFactory.NOP();
	}
	
	private void _int(String operands)
	{
		if ( isImmediate(operands) )
		{
			int imm = getImmediate(operands);
			draft = InstructionFactory.INT(imm);
		}
		else
		{
			// error
		}
	}
	
	private void _generic(InstructionSet inst, String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			return; // error
		}
		
		String target = tokens[0];
		Select reg1 = getRegister(target);
		if ( reg1 == null )
		{
			return; // error
		}
		
		String source = tokens[1];
		Select reg2 = getRegister(source);
		if ( isRegister(source) )
		{
			if ( reg2 == null )
			{
				return; // error
			}
			
			draft = InstructionFactory.GEN_SS(inst, reg1, reg2);
		}
		else
		if ( isImmediate(source) )
		{
			int imm = getImmediate(source);
			draft = InstructionFactory.GEN_SI(inst, reg1, imm);
		}
		else
		{
			// error
		}
	}
	
	private void _jump(InstructionSet inst, String operands)
	{
		label = operands;
		draft = InstructionFactory.GEN_JMP(inst);
	}
	
	
	
	
	
	
	/*
	 * push [reg8]					PUSH, Rx
	 * push [reg16]					PUSH, Rx
	 * push [imm8]					PUSH, I, (byte)0x00,
	 * push &[addr16]				PUSH, M, (byte)0x00,(byte)0x00,
	 
	protected void _push(String operands)
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
	
	//*/
	
}
