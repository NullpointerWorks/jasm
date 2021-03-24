package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SystemTranslator implements CodeTranslator
{
	private List<Translation> translation;
	private BuildError error;
	
	private final String int_syntax = 
			"\n  int <val>";
	
	public SystemTranslator()
	{
		error = null;
		translation = new ArrayList<Translation>();
	}
	
	@Override
	public boolean hasErrors()
	{
		return error != null;
	}

	@Override
	public BuildError getError() 
	{
		return error;
	}

	@Override
	public boolean hasOperation(String instruct)
	{
		if (instruct.equals("nop")) return true;
		if (instruct.equals("int")) return true;
		return false;
	}
	
	@Override
	public List<Translation> getTranslation(SourceCode sc) 
	{
		translation.clear();
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		if (instruct.equals("nop"))
		{
			buildNOP(sc);
		}
		else
		if (instruct.equals("int"))
		{
			buildINT(sc, operands);
		}
		
		return translation;
	}
	
	protected void buildNOP(SourceCode sc) 
	{
		Translation t = new Translation(sc);
		t.setInstruction(Instruction.NOP);
		translation.add(t);
	}
	
	protected void buildINT(SourceCode sc, String operand) 
	{
		if (operand.contains(","))
		{
			error = new TranslationError(sc, "  Syntax error: Interrupt instructions only accept one operand."+int_syntax);
			return;
		}
		
		Operand op = new Operand(operand); // allowed to be a number, or a definition label
		if (!op.isNumber())
		{
			if (!op.isLabel())
			{
				error = new TranslationError(sc, "  Syntax error: Interrupts only accept numbers or definitions."+int_syntax);
				return;
			}
		}
		if (op.isAddress())
		{
			error = new TranslationError(sc, "  Syntax error: Interrupts do not accept addresses."+int_syntax);
			return;
		}
		
		Translation t = new Translation(sc);
		t.setInstruction(Instruction.INT);
		t.setOperand(op);
		translation.add(t);
	}
}
