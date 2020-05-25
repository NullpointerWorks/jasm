package com.nullpointerworks.jasm.prebuild;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.Draft;
import com.nullpointerworks.jasm.compiler.SourceCode;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.controlflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.dataflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.system.*;
import com.nullpointerworks.util.StringUtil;

public class InstructionDraft implements Draft<Instruction>
{
	private SourceCode loc = null;
	
	private Instruction instruction = null;
	private String label = "";
	private int code_index = 0;
	
	public InstructionDraft(int index, SourceCode loc)
	{
		this.loc=loc;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		draft(index,instruct,operands);
	}
	
	public SourceCode getLineOfCode() {return loc;}
	
	public Instruction getInstruction() {return instruction;}
	
	public final boolean hasLabel() {return label.length() > 0;}
	
	public final String getLabel() {return label;}

	public final void setJumpAddress(int addr) {instruction.setJumpAddress(addr);}
	
	public final int getCodeIndex() {return code_index;}
	
	public boolean hasError() {return false;}
	
	public BuildError getError() {return null;}

	/*
	 * ===========================================================
	 */
	
	private void draft(int index, String instruct, String operands)
	{
		code_index = index;
		
		/*
		 * system
		 */
		if (instruct.equals("nop")) {_nop(); return;}
		if (instruct.equals("int")) {_int(operands); return;}
		
		/*
		 * arithmetic
		 */
		if (instruct.equals("add")) {_add(operands); return;}
		if (instruct.equals("sub")) {_sub(operands); return;}
		if (instruct.equals("cmp")) {_cmp(operands); return;}
		if (instruct.equals("shl")) {_shl(operands); return;}
		if (instruct.equals("shr")) {_shr(operands); return;}
		if (instruct.equals("inc")) {_inc(operands); return;}
		if (instruct.equals("dec")) {_dec(operands); return;}
		if (instruct.equals("neg")) {_neg(operands); return;}
		
		/*
		 * data transfer
		 */
		if (instruct.equals("load")) {_load(operands); return;}
		if (instruct.equals("push")) {_push(operands); return;}
		if (instruct.equals("pop")) {_pop(operands); return;}
		if (instruct.equals("sto")) {_sto(operands); return;}
		if (instruct.equals("read")) {_read(operands); return;}
		
		/*
		 * control flow
		 */
		if (instruct.equals("call")) {_call(operands); return;}
		if (instruct.equals("jmp")) {_jump(operands); return;}
		if (instruct.equals("je")) {_jequal(operands); return;}
		if (instruct.equals("jne")) {_jnotequal(operands); return;}
		if (instruct.equals("jl")) {_jless(operands); return;}
		if (instruct.equals("jle")) {_jlessequal(operands); return;}
		if (instruct.equals("jg")) {_jgreater(operands); return;}
		if (instruct.equals("jge")) {_jgreaterequal(operands); return;}
		if (instruct.equals("ret")) {_return(); return;}
	}
	
	/* ================================================================================
	 * 
	 * 		system
	 * 
	 * ============================================================================= */

	/*
	 * nop
	 */
	private void _nop()
	{
		instruction = new NoOperation();
	}
	
	/*
	 * int <imm>
	 */
	private void _int(String operands)
	{
		if ( isImmediate(operands) )
		{
			int imm = getImmediate(operands);
			instruction = new Interrupt(imm);
		}
		else
		{
			// error
		}
	}

	/* ================================================================================
	 * 
	 * 		arithmetic
	 * 
	 * ============================================================================= */
	
	/*
	 * add <reg>,<reg>
	 * add <reg>,<imm>
	 */
	private void _add(String operands)
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
			
			instruction = new Addition_SS(reg1,reg2);
		}
		else
		if ( isImmediate(source) )
		{
			int imm = getImmediate(source);
			instruction = new Addition_SI(reg1,imm);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * sub <reg>,<reg>
	 * sub <reg>,<imm>
	 */
	private void _sub(String operands)
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
			
			instruction = new Subtract_SS(reg1,reg2);
		}
		else
		if ( isImmediate(source) )
		{
			int imm = getImmediate(source);
			instruction = new Subtract_SI(reg1,imm);
		}
		else
		{
			// error
		}
	}

	/*
	 * cmp <reg>,<reg>
	 * cmp <reg>,<imm>
	 */
	private void _cmp(String operands)
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
			
			instruction = new Compare_SS(reg1,reg2);
		}
		else
		if ( isImmediate(source) )
		{
			int imm = getImmediate(source);
			instruction = new Compare_SI(reg1,imm);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * shl <reg>
	 */
	private void _shl(String operands)
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new ShiftLeft_S(reg);
		}
		else
		{
			// error
		}
	}

	/*
	 * shr <reg>
	 */
	private void _shr(String operands)
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new ShiftRight_S(reg);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * inc <reg>
	 */
	private void _inc(String operands)
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Increment_S(reg);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * dec <reg>
	 */
	private void _dec(String operands)
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Decrement_S(reg);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * neg <reg>
	 */
	private void _neg(String operands) 
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Negate_S(reg);
		}
		else
		{
			// error
		}
	}

	/* ================================================================================
	 * 
	 * 		data transfer
	 * 
	 * ============================================================================= */
	
	/*
	 * load <reg>,<reg>
	 * load <reg>,<imm>
	 */
	private void _load(String operands)
	{
		String[] tokens = split(operands);
		if (tokens==null) return; // error
		
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
			
			instruction = new Load_SS(reg1,reg2);
		}
		else
		if ( isImmediate(source) )
		{
			int imm = getImmediate(source);
			instruction = new Load_SI(reg1,imm);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * push <reg>
	 * push <imm>
	 */
	private void _push(String operands)
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Push_S(reg);
		}
		else
		if ( isImmediate(operands) )
		{
			int imm = getImmediate(operands);
			instruction = new Push_I(imm);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * pop <reg>
	 */
	private void _pop(String operands)
	{
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Pop_S(reg);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * sto @<reg>,<reg>
	 * sto @<imm>,<reg>
	 */
	private void _sto(String operands)
	{
		String[] tokens = split(operands);
		if (tokens==null) return; // error
		
		/*
		 * check destination
		 */
		String source = tokens[1];
		Select regS = getRegister(source);
		if ( regS == null )
		{
			return; // if not register, error
		}
		
		/*
		 * check target location. must be an address
		 */
		String target = tokens[0];
		if (!isAddress(target))
		{
			return; // error
		}
		target = target.substring(1); // remove @ sign
		
		if ( isRegister(target) )
		{
			Select regT = getRegister(target);
			instruction = new Store_SS(regT,regS);
		}
		else
		if ( isImmediate(target) )
		{
			int immT = getImmediate(target);
			instruction = new Store_IS(immT,regS);
		}
		else
		{
			// error
		}
	}
	
	/*
	 * read <reg>, @<reg>
	 * read <reg>, @<imm>
	 */
	private void _read(String operands)
	{
		String[] tokens = split(operands);
		if (tokens==null) return; // error
		
		/*
		 * check target
		 */
		String target = tokens[0];
		Select regT = getRegister(target);
		if ( regT == null )
		{
			return; // if not register, error
		}
		
		/*
		 * check source location. must be an address
		 */
		String source = tokens[1];
		if (!isAddress(source))
		{
			return; // error
		}
		source = source.substring(1); // remove @ sign
		
		if ( isRegister(source) )
		{
			Select regS = getRegister(source);
			instruction = new Read_SS(regT,regS);
		}
		else
		if ( isImmediate(source) )
		{
			int immS = getImmediate(source);
			instruction = new Read_SI(regT,immS);
		}
		else
		{
			// error
		}
	}
	
	/* ================================================================================
	 * 
	 * 		control flow
	 * 
	 * ============================================================================= */

	private void _call(String operands)
	{
		label = operands;
		instruction = new Call(0);
	}
	
	private void _jump(String operands)
	{
		label = operands;
		instruction = new Jump(0);
	}
	
	private void _jequal(String operands)
	{
		label = operands;
		instruction = new JumpEqual(0);
	}
	
	private void _jnotequal(String operands)
	{
		label = operands;
		instruction = new JumpNotEqual(0);
	}
	
	private void _jless(String operands)
	{
		label = operands;
		instruction = new JumpLess(0);
	}
	
	private void _jlessequal(String operands)
	{
		label = operands;
		instruction = new JumpLessEqual(0);
	}
	
	private void _jgreater(String operands)
	{
		label = operands;
		instruction = new JumpGreater(0);
	}
	
	private void _jgreaterequal(String operands)
	{
		label = operands;
		instruction = new JumpGreaterEqual(0);
	}
	
	private void _return()
	{
		instruction = new Return();
	}
	
	/*
	 * ===========================================================
	 */
	
	private String[] split(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			return null; // error
		}
		return tokens;
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
	
	/*
	 * ===========================================================
	 */
	
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
		case "x": return Select.REG_X;
		case "y": return Select.REG_Y;
		default: break;
		}
		return null; // error
	}
	
	/*
	 * ===========================================================
	 */
	
	private boolean isAddress(String addr)
	{
		if (addr.startsWith("@")) return true;
		return false;
	}
}
