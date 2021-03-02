package com.nullpointerworks.jasm.assembly.drafting;

import com.nullpointerworks.jasm.assembly.parser.SourceCode;

class ControlFlowBuilder extends AbstractDraftBuilder
{
	private SourceCode source;
	
	public ControlFlowBuilder() {}
	
	public boolean isControlFlow(String instruct) 
	{
		if (instruct.equals("call")) return true;
		if (instruct.equals("jmp")) return true;
		if (instruct.equals("je")) return true;
		if (instruct.equals("jne")) return true;
		if (instruct.equals("jl")) return true;
		if (instruct.equals("jle")) return true;
		if (instruct.equals("jg")) return true;
		if (instruct.equals("jge")) return true;
		if (instruct.equals("ret")) return true;
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
		
		throwError("  Unrecognized instruction or parameters");
		return null;
	}
	
	private Draft _generic_jump(Operation op, String operand)
	{
		var d = new Draft(source, op);
		d.setLabel(operand);
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
	
	private void throwError(String str) 
	{
		super.setError(source, str);
	}
}