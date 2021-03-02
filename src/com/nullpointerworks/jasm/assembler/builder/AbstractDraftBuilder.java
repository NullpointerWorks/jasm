package com.nullpointerworks.jasm.assembler.builder;

import com.nullpointerworks.jasm.assembler.Draft;
import com.nullpointerworks.jasm.assembler.Operand;
import com.nullpointerworks.jasm.assembler.Operation;
import com.nullpointerworks.jasm.assembler.SourceCode;
import com.nullpointerworks.jasm.assembler.errors.AssemblerError;
import com.nullpointerworks.jasm.assembler.errors.BuildError;

abstract class AbstractDraftBuilder 
{
	private BuildError error;

	public boolean hasError() {return error != null;}
	public BuildError getError() {return error;}
	
	protected void setError(BuildError e) 
	{
		error = e;
	}
	
	protected void setError(SourceCode source, String str) 
	{
		if (error == null) error = new AssemblerError(source, str);
	}
	
	/*
	 * ===========================================================
	 */
	
	/*
	 * gen <reg>,<reg>
	 * gen <reg>,<imm>
	 */
	protected Draft _generic_instruction(SourceCode source, Operation op, String operands, String oper, String syntax)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			setError(source, "  Syntax error: "+oper+" instructions use two operands."+syntax);
			return null; // error
		}
		
		Operand op1 = new Operand(tokens[0]);
		if (!op1.isRegister())
		{
			setError(source, "  Syntax error: "+oper+" target operand must be a register."+syntax);
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
			setError(source, "  Syntax error: "+oper+" source operand must be either a register or an immediate value."+syntax);
			// error
		}
		
		return d;
	}
	
	/*
	 * gen <reg>
	 */
	protected Draft _generic_register(SourceCode source, Operation op, String operands, String oper, String syntax)
	{
		if (operands.contains(","))
		{
			setError(source, "  Syntax error: "+oper+" instructions accept only one operand."+syntax);
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
			setError(source, "  Syntax error: "+oper+" target operand must be a register."+syntax);
		}
		
		return d;
	}
}
