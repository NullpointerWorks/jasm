package com.nullpointerworks.jasm.compiler.prebuild;

import com.nullpointerworks.jasm.compiler.AbstractCompiler;
import com.nullpointerworks.jasm.compiler.Draft;
import com.nullpointerworks.jasm.compiler.Operation;
import com.nullpointerworks.jasm.compiler.SourceCode;
import com.nullpointerworks.jasm.compiler.errors.CompilerError;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.instruction.system.Interrupt;

/**
 * 
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class InstructionCompiler extends AbstractCompiler<Instruction>
{
	private SourceCode loc;
	
	/*
	 * OP 
	 * OP label
	 * OP reg
	 * OP imm
	 * OP reg reg
	 * OP reg imm
	 * 
	 * 
	 */
	
	@Override
	public Draft<Instruction> draft(SourceCode loc, Operation op, String operands) 
	{
		this.loc = loc;
		
		switch(op)
		{
		case NOP: 
			
		}
		
		
		return null;
	}
	
	/* ================================================================================
	 * 
	 * 		system
	 * 
	 * ============================================================================= */
	
	private final String int_syntax = "\n  int <imm>";
	
	/*
	 * int <imm>
	 */
	private void _int(String operands)
	{
		if (operands.contains(","))
		{
			addCompilerError(loc, "  Syntax error: Interrupt instructions only accept one operand."+int_syntax);
			return;
		}
		
		if ( isImmediate(operands) )
		{
			int imm = getImmediate(operands);
			instruction = new Interrupt(imm);
		}
		else
		{
			addCompilerError(loc, "  Syntax error: Interrupts only accept immediate values."+int_syntax);
		}
	}
	
	

	
	/*
	 * ===========================================================
	 */
	
	private boolean isAddress(String addr)
	{
		if (addr.startsWith(ADDRESS_MARK)) return true;
		return false;
	}
	
}
