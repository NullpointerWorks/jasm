package com.nullpointerworks.jasm.assembler.builder;

import com.nullpointerworks.jasm.assembler.Draft;
import com.nullpointerworks.jasm.assembler.Operand;
import com.nullpointerworks.jasm.assembler.Operation;
import com.nullpointerworks.jasm.assembler.SourceCode;

public class ArithmeticBuilder extends AbstractDraftBuilder
{
	private SourceCode source;
	
	public ArithmeticBuilder() {}
	
	public boolean isArithmetic(String instruct) 
	{
		if (instruct.equals("add")) return true;
		if (instruct.equals("sub")) return true;
		if (instruct.equals("cmp")) return true;
		if (instruct.equals("shl")) return true;
		if (instruct.equals("shr")) return true;
		if (instruct.equals("inc")) return true;
		if (instruct.equals("dec")) return true;
		if (instruct.equals("neg")) return true;
		return false;
	}
	
	public Draft[] getDraft(SourceCode loc)
	{
		setError(source, null);
		source = loc;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		return new Draft[] {getDraft(instruct, operands)};
	}
	
	// ================================================================================
	
	private Draft getDraft(String instruct, String operands)
	{
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
		
		setError("  Unrecognized instruction or parameters");
		return null;
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
	 * ===========================================================
	 */
	
	private void setError(String str) 
	{
		super.setError(source, str);
	}
}
