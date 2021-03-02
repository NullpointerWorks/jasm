package com.nullpointerworks.jasm.assembler.builder;

import com.nullpointerworks.jasm.assembler.Draft;
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
		setError(null);
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
		
		throwError("  Unrecognized instruction or parameters");
		return _nop();
	}
	
	/*
	 * nop
	 */
	private Draft _nop()
	{
		Draft d = new Draft(source, Operation.NOP);
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
		return _generic_instruction(source, Operation.ADD, operands, "Addition", add_syntax);
	}
	
	/*
	 * sub <reg>,<reg>
	 * sub <reg>,<imm>
	 */
	private final String sub_syntax = "\n  sub <reg>,<reg>\n  sub <reg>,<imm>";
	private Draft _sub(String operands)
	{
		return _generic_instruction(source, Operation.SUB, operands, "Subtraction", sub_syntax);
	}
	
	/*
	 * cmp <reg>,<reg>
	 * cmp <reg>,<imm>
	 */
	private final String cmp_syntax = "\n  cmp <reg>,<reg>\n  cmp <reg>,<imm>";
	private Draft _cmp(String operands)
	{
		return _generic_instruction(source, Operation.CMP, operands, "Compare", cmp_syntax);
	}
	
	/*
	 * shl <reg>
	 */
	private final String shl_syntax = "\n  shl <reg>";
	private Draft _shl(String operands)
	{
		return _generic_register(source, Operation.SHL, operands, "Bitshift", shl_syntax);
	}

	/*
	 * shr <reg>
	 */
	private final String shr_syntax = "\n  shr <reg>";
	private Draft _shr(String operands)
	{
		return _generic_register(source, Operation.SHR, operands, "Bitshift", shr_syntax);
	}
	
	/*
	 * inc <reg>
	 */
	private final String inc_syntax = "\n  inc <reg>";
	private Draft _inc(String operands)
	{
		return _generic_register(source, Operation.INC, operands, "Increment", inc_syntax);
	}
	
	/*
	 * dec <reg>
	 */
	private final String dec_syntax = "\n  dec <reg>";
	private Draft _dec(String operands)
	{
		return _generic_register(source, Operation.DEC, operands, "Decrement", dec_syntax);
	}
	
	/*
	 * neg <reg>
	 */
	private final String neg_syntax = "\n  neg <reg>";
	private Draft _neg(String operands) 
	{
		return _generic_register(source, Operation.NEG, operands, "Negate", neg_syntax);
	}

	/*
	 * ===========================================================
	 */
	
	private void throwError(String str) 
	{
		super.setError(source, str);
	}
}
