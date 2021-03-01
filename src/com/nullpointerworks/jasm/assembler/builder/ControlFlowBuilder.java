package com.nullpointerworks.jasm.assembler.builder;

import com.nullpointerworks.jasm.assembler.Draft;
import com.nullpointerworks.jasm.assembler.Operation;
import com.nullpointerworks.jasm.assembler.SourceCode;
import com.nullpointerworks.jasm.assembler.errors.AssemblerError;
import com.nullpointerworks.jasm.assembler.errors.BuildError;

public class ControlFlowBuilder
{
	private SourceCode source;
	private BuildError error;
	
	public ControlFlowBuilder() {}
	public boolean hasError() {return error != null;}
	public BuildError getError() {return error;}
	
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
		error = null;
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
		
		setError("  Unrecognized instruction or parameters");
		return null;
	}
	
	private Draft _generic_jump(Operation op, String operand)
	{
		var d = new Draft(source, op);
		d.setLabel(operand);
		return d;
	}
	
	private void setError(String str) 
	{
		if (error == null) error = new AssemblerError(source, str);
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
}
