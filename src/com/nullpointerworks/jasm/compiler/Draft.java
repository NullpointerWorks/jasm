package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.compiler.Draft;
import com.nullpointerworks.jasm.compiler.errors.BuildError;
import com.nullpointerworks.jasm.compiler.errors.CompilerError;

import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.controlflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.dataflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.system.*;

import com.nullpointerworks.util.StringUtil;

/**
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class Draft
{	
	private SourceCode source;
	private BuildError err;
	private Register address; // reference to set a jump address after parsing
	private Instruction instruction;
	private String label = "";
	
	public Draft(SourceCode loc)
	{
		address = new Register(0);
		source = loc;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
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
		
		setError("  Unrecognized instruction or parameters");
	}
	
	/*
	 * instruction
	 */
	public SourceCode getLineOfCode() {return source;}
	
	public Instruction getInstruction() {return instruction;}
	
	/*
	 * labels
	 */
	public final boolean hasLabel() {return label.length() > 0;}
	
	public final String getLabel() {return label;}
	
	public final void setJumpAddress(int addr) {address.setValue(addr);}
	
	/*
	 * errors
	 */
	public boolean hasError() {return err != null;}
	
	public BuildError getError() {return err;}
	
	/* ================================================================================
	 * 
	 * 		system
	 * 
	 * ============================================================================= */
	
	private final String int_syntax = "\n  int <imm>";
	
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
		if (operands.contains(","))
		{
			setError("  Syntax error: Interrupt instructions only accept one operand."+int_syntax);
			return;
		}
		
		if ( isImmediate(operands) )
		{
			int imm = getImmediate(operands);
			instruction = new Interrupt(imm);
		}
		else
		{
			setError("  Syntax error: Interrupts only accept immediate values."+int_syntax);
		}
	}

	/* ================================================================================
	 * 
	 * 		arithmetic
	 * 
	 * ============================================================================= */

	private final String add_syntax = "\n  add <reg>,<reg>\n  add <reg>,<imm>";
	private final String sub_syntax = "\n  sub <reg>,<reg>\n  sub <reg>,<imm>";
	private final String cmp_syntax = "\n  cmp <reg>,<reg>\n  cmp <reg>,<imm>";
	private final String shl_syntax = "\n  shl <reg>";
	private final String shr_syntax = "\n  shr <reg>";
	private final String inc_syntax = "\n  inc <reg>";
	private final String dec_syntax = "\n  dec <reg>";
	private final String neg_syntax = "\n  neg <reg>";
	
	/*
	 * add <reg>,<reg>
	 * add <reg>,<imm>
	 */
	private void _add(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: Addition instructions use two operands."+add_syntax);
			return; // error
		}
		
		String target = tokens[0];
		Select reg1 = getRegister(target);
		if ( reg1 == null )
		{
			setError("  Syntax error: Addition target operand must be a register."+add_syntax);
			return; // error
		}
		
		String source = tokens[1];
		Select reg2 = getRegister(source);
		if ( reg2 != null )
		{
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
			setError("  Syntax error: Addition source operand must be either a register or an immediate value."+add_syntax);
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
			setError("  Syntax error: Subtraction instructions use two operands."+sub_syntax);
			return; // error
		}
		
		String target = tokens[0];
		Select reg1 = getRegister(target);
		if ( reg1 == null )
		{
			setError("  Syntax error: Subtraction target operand must be a register."+sub_syntax);
			return; // error
		}
		
		String source = tokens[1];
		Select reg2 = getRegister(source);
		if ( reg2 != null )
		{
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
			setError("  Syntax error: Subtraction source operand must be either a register or an immediate value."+sub_syntax);
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
			setError("  Syntax error: Compare instructions use two operands."+cmp_syntax);
			return; // error
		}
		
		String target = tokens[0];
		Select reg1 = getRegister(target);
		if ( reg1 == null )
		{
			setError("  Syntax error: Compare target operand must be a register."+cmp_syntax);
			return; // error
		}
		
		String source = tokens[1];
		Select reg2 = getRegister(source);
		if ( reg2 != null )
		{
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
			setError("  Syntax error: Compare source operand must be either a register or an immediate value."+cmp_syntax);
			// error
		}
	}
	
	/*
	 * shl <reg>
	 */
	private void _shl(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Bitshift instructions accept only one operand."+shl_syntax);
			return;
		}
		
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new ShiftLeft_S(reg);
		}
		else
		{
			setError("  Syntax error: Bitshift target operand must be a register."+shl_syntax);
			// error
		}
	}

	/*
	 * shr <reg>
	 */
	private void _shr(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Bitshift instructions accept only one operand."+shr_syntax);
			return;
		}
		
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new ShiftRight_S(reg);
		}
		else
		{
			setError("  Syntax error: Bitshift target operand must be a register."+shr_syntax);
		}
	}
	
	/*
	 * inc <reg>
	 */
	private void _inc(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Increment instructions accept only one operand."+inc_syntax);
			return;
		}
		
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Increment_S(reg);
		}
		else
		{
			setError("  Syntax error: Increment target operand must be a register."+inc_syntax);
		}
	}
	
	/*
	 * dec <reg>
	 */
	private void _dec(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Decrement instructions accept only one operand."+dec_syntax);
			return;
		}
		
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Decrement_S(reg);
		}
		else
		{
			setError("  Syntax error: Decrement target operand must be a register."+dec_syntax);
		}
	}
	
	/*
	 * neg <reg>
	 */
	private void _neg(String operands) 
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Negation instructions accept only one operand."+neg_syntax);
			return;
		}
		
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Negate_S(reg);
		}
		else
		{
			setError("  Syntax error: Negation target operand must be a register."+neg_syntax);
		}
	}

	/* ================================================================================
	 * 
	 * 		data transfer
	 * 
	 * ============================================================================= */
	
	private final String load_syntax = "\n  load <reg>,<reg>\n  load <reg>,<imm>";
	private final String push_syntax = "\n  push <reg>\n  push <imm>";
	private final String pop_syntax = "\n  pop <reg>";
	private final String sto_syntax = "\n  sto @<reg>,<reg>\n  sto @<imm>,<reg>";
	private final String read_syntax = "\n  read <reg>,@<reg>\n  read <reg>,@<imm>";
	
	/*
	 * load <reg>,<reg>
	 * load <reg>,<imm>
	 */
	private void _load(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: Load instructions use two operands."+load_syntax);
			return; // error
		}
		
		String target = tokens[0];
		Select reg1 = getRegister(target);
		if ( reg1 == null )
		{
			setError("  Syntax error: Load target operand must be a register."+load_syntax);
			return; // error
		}
		
		String source = tokens[1];
		Select reg2 = getRegister(source);
		if ( reg2 != null )
		{
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
			setError("  Syntax error: Load source operand must be either a register or an immediate value."+load_syntax);
			// error
		}
	}
	
	/*
	 * push <reg>
	 * push <imm>
	 */
	private void _push(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Stack instructions accept only one operand."+push_syntax);
			return;
		}
		
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
			setError("  Syntax error: Push operand must be either a register or an immediate value."+push_syntax);
		}
	}
	
	/*
	 * pop <reg>
	 */
	private void _pop(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Stack instructions accept only one operand."+pop_syntax);
			return;
		}
		
		if ( isRegister(operands) )
		{
			Select reg = getRegister(operands);
			instruction = new Pop_S(reg);
		}
		else
		{
			setError("  Syntax error: Pop operand must be a register."+pop_syntax);
		}
	}
	
	/*
	 * sto @<reg>,<reg>
	 * sto @<imm>,<reg>
	 */
	private void _sto(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: Store instructions use two operands."+sto_syntax);
			return; // error
		}
		
		/*
		 * check source
		 */
		String source = tokens[1];
		Select regS = getRegister(source);
		if ( regS == null )
		{
			setError("  Syntax error: Store source operand must be a register."+sto_syntax);
			return; // if not register, error
		}
		
		/*
		 * check target location. must be an address
		 */
		String target = tokens[0];
		if (!isAddress(target))
		{
			setError("  Syntax error: Store target operand must be an address."+sto_syntax);
			return; // error
		}
		target = target.substring(1); // remove @ sign

		Select regT = getRegister(target);
		if ( regT!=null )
		{
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
			setError("  Syntax error: Store target operand must be either a register or an immediate value."+sto_syntax);
			// error
		}
	}
	
	/*
	 * read <reg>,@<reg>
	 * read <reg>,@<imm>
	 */
	private void _read(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: Read instructions use two operands."+read_syntax);
			return; // error
		}
		
		/*
		 * check target
		 */
		String target = tokens[0];
		Select regT = getRegister(target);
		if ( regT == null )
		{
			setError("  Syntax error: Read target operand must be a register."+read_syntax);
			return; // if not register, error
		}
		
		/*
		 * check source location. must be an address
		 */
		String source = tokens[1];
		if (!isAddress(source))
		{
			setError("  Syntax error: Read source operand must be an address."+read_syntax);
			return; // error
		}
		source = source.substring(1); // remove @ sign
		
		Select regS = getRegister(source);
		if ( regS != null )
		{
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
			setError("  Syntax error: Read source operand must be either a register or an immediate value."+read_syntax);
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
		instruction = new Call(address);
	}
	
	private void _jump(String operands)
	{
		label = operands;
		instruction = new Jump(address);
	}
	
	private void _jequal(String operands)
	{
		label = operands;
		instruction = new JumpEqual(address);
	}
	
	private void _jnotequal(String operands)
	{
		label = operands;
		instruction = new JumpNotEqual(address);
	}
	
	private void _jless(String operands)
	{
		label = operands;
		instruction = new JumpLess(address);
	}
	
	private void _jlessequal(String operands)
	{
		label = operands;
		instruction = new JumpLessEqual(address);
	}
	
	private void _jgreater(String operands)
	{
		label = operands;
		instruction = new JumpGreater(address);
	}
	
	private void _jgreaterequal(String operands)
	{
		label = operands;
		instruction = new JumpGreaterEqual(address);
	}
	
	private void _return()
	{
		instruction = new Return();
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
	
	/*
	 * ===========================================================
	 */
	
	private void setError(String str) 
	{
		if (err == null) err = new CompilerError(source, str);
	}
	
	private boolean isAddress(String addr)
	{
		if (addr.startsWith("@")) return true;
		return false;
	}
}
