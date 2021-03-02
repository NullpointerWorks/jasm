package com.nullpointerworks.jasm.assembly.drafting;

import com.nullpointerworks.jasm.assembly.parser.SourceCode;

class SystemCallBuilder extends AbstractDraftBuilder
{
	private SourceCode source;
	
	public SystemCallBuilder() {}
	
	public boolean isSystemCall(String instruct) 
	{
		if (instruct.equals("nop")) return true;
		if (instruct.equals("int")) return true;
		return false;
	}
	
	public Draft[] getDraft(SourceCode loc)
	{
		source = loc;
		setError(null);
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
		 * data flow
		 */
		if (instruct.equals("nop")) {return _nop();}
		if (instruct.equals("int")) {return _int(operands);}
		
		throwError("  Unrecognized instruction or parameters");
		return null;
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
			throwError("  Syntax error: Interrupt instructions only accept one operand."+int_syntax);
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
			throwError("  Syntax error: Interrupts only accept immediate values."+int_syntax);
		}
		
		return d;
	}
	
	/*
	 * ===========================================================
	 */
	
	private void throwError(String str) 
	{
		super.setError(source, str);
	}
}
