package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.compiler.errors.BuildError;
import com.nullpointerworks.jasm.compiler.errors.CompilerError;

/**
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class DraftBuilder
{	
	private SourceCode source;
	private BuildError error;
	
	public boolean hasError() {return error != null;}
	public void setError(BuildError e) {error = e;}
	public BuildError getError() {return error;}
	
	public Draft getDraft(SourceCode loc)
	{
		error = null;
		source = loc;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		/*
		 * system
		 */
		if (instruct.equals("nop")) {return _nop();}
		if (instruct.equals("int")) {return _int(operands);}
		
		/*
		 * arithmetic
		 */
		if (instruct.equals("add")) {return _add(operands);}
		if (instruct.equals("sub")) {return _sub(operands);}
		if (instruct.equals("cmp")) {return _cmp(operands);}
		if (instruct.equals("shl")) {return _shl(operands);}
		if (instruct.equals("shr")) {return _shr(operands);}
		if (instruct.equals("inc")) {return _inc(operands);}
		if (instruct.equals("dec")) {return _dec(operands);}
		if (instruct.equals("neg")) {return _neg(operands);}
		
		/*
		 * data transfer
		 */
		if (instruct.equals("load")) {return _load(operands);}
		if (instruct.equals("push")) {return _push(operands);}
		if (instruct.equals("pop")) {return _pop(operands);}
		if (instruct.equals("sto")) {return _sto(operands);}
		if (instruct.equals("read")) {return _read(operands);}
		
		/*
		 * control flow
		 */
		if (instruct.equals("call")) {return _call(operands);}
		if (instruct.equals("jmp")) {return _jump(operands);}
		if (instruct.equals("je")) {return _jequal(operands);}
		if (instruct.equals("jne")) {return _jnotequal(operands);}
		if (instruct.equals("jl")) {return _jless(operands);}
		if (instruct.equals("jle")) {return _jlessequal(operands);}
		if (instruct.equals("jg")) {return _jgreater(operands);}
		if (instruct.equals("jge")) {return _jgreaterequal(operands);}
		if (instruct.equals("ret")) {return _return();}
		
		setError("  Unrecognized instruction or parameters");
		return _nop();
	}

	/* ================================================================================
	 * 
	 * 		generic builder methods
	 * 
	 * ============================================================================= */
	
	/*
	 * gen <reg>,<reg>
	 * gen <reg>,<imm>
	 */
	private Draft _generic_instruction(Operation op, String operands, String oper, String syntax)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: "+oper+" instructions use two operands."+syntax);
			return null; // error
		}
		
		Operand op1 = new Operand(tokens[0]);
		if (!op1.isRegister())
		{
			setError("  Syntax error: "+oper+" target operand must be a register."+syntax);
			return null; // error
		}
		
		Draft d = new Draft(source, op);
		d.addOperand(op1);
		
		Operand op2 = new Operand(tokens[1]);
		if (!op2.hasError())
		{
			d.addOperand(op2);
		}
		else
		{
			setError("  Syntax error: "+oper+" source operand must be either a register or an immediate value."+syntax);
			// error
		}
		
		return d;
	}
	
	/*
	 * gen <reg>
	 */
	private Draft _generic_register(Operation op, String operands, String oper, String syntax)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: "+oper+" instructions accept only one operand."+syntax);
			return null;
		}
		
		Draft d = new Draft(source, op);
		Operand op1 = new Operand(operands);
		if (op1.isRegister())
		{
			d.addOperand(op1);
		}
		else
		{
			setError("  Syntax error: "+oper+" target operand must be a register."+syntax);
		}
		
		return d;
	}
	
	/*
	 * gen
	 */
	private Draft _generic_jump(Operation op, String operand)
	{
		var d = new Draft(source, op);
		d.setLabel(operand);
		return d;
	}
	
	/* ================================================================================
	 * 
	 * 		system
	 * 
	 * ============================================================================= */
	
	private final String int_syntax = "\n  int <imm>";
	
	/*
	 * nop
	 */
	private Draft _nop()
	{
		Draft d = new Draft(source, Operation.NOP);
		return d;
	}
	
	/*
	 * int <imm>
	 */
	private Draft _int(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Interrupt instructions only accept one operand."+int_syntax);
			return null;
		}
		
		Draft d = new Draft(source, Operation.INT);
		Operand op = new Operand(operands);
		if (op.isImmediate())
		{
			d.addOperand(op);
		}
		else
		{
			setError("  Syntax error: Interrupts only accept immediate values."+int_syntax);
		}
		
		return d;
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
	private final String add_syntax = "\n  add <reg>,<reg>\n  add <reg>,<imm>";
	private Draft _add(String operands)
	{
		return _generic_instruction(Operation.ADD, operands, "Addition", add_syntax);
	}
	
	/*
	 * sub <reg>,<reg>
	 * sub <reg>,<imm>
	 */
	private final String sub_syntax = "\n  sub <reg>,<reg>\n  sub <reg>,<imm>";
	private Draft _sub(String operands)
	{
		return _generic_instruction(Operation.SUB, operands, "Subtraction", sub_syntax);
	}
	
	/*
	 * cmp <reg>,<reg>
	 * cmp <reg>,<imm>
	 */
	private final String cmp_syntax = "\n  cmp <reg>,<reg>\n  cmp <reg>,<imm>";
	private Draft _cmp(String operands)
	{
		return _generic_instruction(Operation.CMP, operands, "Compare", cmp_syntax);
	}
	
	/*
	 * shl <reg>
	 */
	private final String shl_syntax = "\n  shl <reg>";
	private Draft _shl(String operands)
	{
		return _generic_register(Operation.SHL, operands, "Bitshift", shl_syntax);
	}

	/*
	 * shr <reg>
	 */
	private final String shr_syntax = "\n  shr <reg>";
	private Draft _shr(String operands)
	{
		return _generic_register(Operation.SHR, operands, "Bitshift", shr_syntax);
	}
	
	/*
	 * inc <reg>
	 */
	private final String inc_syntax = "\n  inc <reg>";
	private Draft _inc(String operands)
	{
		return _generic_register(Operation.INC, operands, "Increment", inc_syntax);
	}
	
	/*
	 * dec <reg>
	 */
	private final String dec_syntax = "\n  dec <reg>";
	private Draft _dec(String operands)
	{
		return _generic_register(Operation.DEC, operands, "Decrement", dec_syntax);
	}
	
	/*
	 * neg <reg>
	 */
	private final String neg_syntax = "\n  neg <reg>";
	private Draft _neg(String operands) 
	{
		return _generic_register(Operation.NEG, operands, "Negate", neg_syntax);
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
	private final String load_syntax = "\n  load <reg>,<reg>\n  load <reg>,<imm>";
	private Draft _load(String operands)
	{
		return _generic_instruction(Operation.LOAD, operands, "Load", load_syntax);
	}
	
	/*
	 * push <reg>
	 * push <imm>
	 */
	private final String push_syntax = "\n  push <reg>\n  push <imm>";
	private Draft _push(String operands)
	{
		if (operands.contains(","))
		{
			setError("  Syntax error: Stack instructions accept only one operand."+push_syntax);
			return null;
		}
		
		Draft d = new Draft(source, Operation.PUSH);
		Operand op1 = new Operand(operands);
		if (!op1.hasError())
		{
			d.addOperand(op1);
		}
		else
		{
			setError("  Syntax error: Stack target operand must be a register."+push_syntax);
		}
		
		return d;
	}
	
	/*
	 * pop <reg>
	 */
	private final String pop_syntax = "\n  pop <reg>";
	private Draft _pop(String operands)
	{
		return _generic_register(Operation.POP, operands, "Pop", pop_syntax);
	}
	
	/*
	 * sto @<reg>,<reg>
	 * sto @<imm>,<reg>
	 */
	private final String sto_syntax = "\n  sto @<reg>,<reg>\n  sto @<imm>,<reg>";
	private Draft _sto(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: Store instructions use two operands."+sto_syntax);
			return null;
		}
		
		Draft d = new Draft(source, Operation.STO);
		
		/*
		 * check source
		 */
		Operand op2 = new Operand(tokens[1]);
		if (!op2.isRegister())
		{
			setError("  Syntax error: Store source operand must be a register."+sto_syntax);
			return null;
		}
		
		/*
		 * check target location. must be an address
		 */
		String target = tokens[0];
		if (!isAddress(target))
		{
			setError("  Syntax error: Store target operand must be an address."+sto_syntax);
			return null;
		}
		target = target.substring(1); // remove @ sign
		
		Operand op1 = new Operand(target);
		if (!op1.hasError())
		{
			d.addOperand(op1);
			d.addOperand(op2);
		}
		else
		{
			setError("  Syntax error: Store target operand must be either a register or an immediate value."+sto_syntax);
		}
		
		return d;
	}
	
	/*
	 * read <reg>,@<reg>
	 * read <reg>,@<imm>
	 */
	private final String read_syntax = "\n  read <reg>,@<reg>\n  read <reg>,@<imm>";
	private Draft _read(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError("  Syntax error: Read instructions use two operands."+read_syntax);
			return null;
		}
		
		Draft d = new Draft(source, Operation.READ);
		
		/*
		 * check target
		 */
		Operand op1 = new Operand(tokens[0]);
		if (!op1.isRegister())
		{
			setError("  Syntax error: Read target operand must be a register."+read_syntax);
			return null;
		}
		d.addOperand(op1);
		
		/*
		 * check source location. must be an address
		 */
		String source = tokens[1];
		if (!isAddress(source))
		{
			setError("  Syntax error: Read source operand must be an address."+read_syntax);
			return null; // error
		}
		source = source.substring(1); // remove @ sign
		
		Operand op2 = new Operand(source);
		if (!op2.hasError())
		{
			d.addOperand(op2);
		}
		else
		{
			setError("  Syntax error: Read source operand must be either a register or an immediate value."+read_syntax);
		}
		return d;
	}
	
	/* ================================================================================
	 * 
	 * 		control flow
	 * 
	 * ============================================================================= */
	
	private Draft _call(String operands)
	{
		return _generic_jump(Operation.CALL, operands);
	}
	
	private Draft _jump(String operands)
	{
		return _generic_jump(Operation.JMP, operands);
	}
	
	private Draft _jequal(String operands)
	{
		return _generic_jump(Operation.JE, operands);
	}
	
	private Draft _jnotequal(String operands)
	{
		return _generic_jump(Operation.JNE, operands);
	}
	
	private Draft _jless(String operands)
	{
		return _generic_jump(Operation.JL, operands);
	}
	
	private Draft _jlessequal(String operands)
	{
		return _generic_jump(Operation.JLE, operands);
	}
	
	private Draft _jgreater(String operands)
	{
		return _generic_jump(Operation.JG, operands);
	}
	
	private Draft _jgreaterequal(String operands)
	{
		return _generic_jump(Operation.JGE, operands);
	}
	
	private Draft _return()
	{
		return new Draft(source, Operation.RET);
	}
	
	/*
	 * ===========================================================
	 */
	
	private void setError(String str) 
	{
		if (error == null) error = new CompilerError(source, str);
	}
	
	private boolean isAddress(String addr)
	{
		if (addr.startsWith("@")) return true;
		return false;
	}
}
