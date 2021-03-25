package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class IntTranslator implements CodeTranslator
{
	private final String syntax = "\n  int <val>";
	private BuildError error;
	
	public IntTranslator()
	{
		error = null;
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
	public boolean isInstruction(String instruct)
	{
		return instruct.equals("int");
	}
	
	@Override
	public List<Translation> getTranslation(SourceCode sc)
	{
		error = null;
		String line = sc.getLine();
		String[] tokens = line.split(" ");
		String operands = "";
		List<Translation> translation = new ArrayList<Translation>();
		
		if (tokens.length == 2)
		{
			operands = tokens[1].toLowerCase();
			translate(sc,operands,translation);
		}
		else
		{
			error = new TranslationError(sc, "  Syntax error: Interrupt take only one argument."+syntax);
		}
		
		return translation;
	}
	
	private void translate(SourceCode sc, String operand, List<Translation> translation) 
	{
		if (operand.contains(","))
		{
			error = new TranslationError(sc, "  Syntax error: Interrupt instructions only accept one operand."+syntax);
			return;
		}
		
		Operand op = new Operand(operand); // allowed to be a number, or a definition label
		if (!op.isNumber())
		{
			if (!op.isLabel())
			{
				error = new TranslationError(sc, "  Syntax error: Interrupts only accept numbers or definitions."+syntax);
				return;
			}
		}
		if (op.isAddress())
		{
			error = new TranslationError(sc, "  Syntax error: Interrupts do not accept addresses."+syntax);
			return;
		}
		
		Translation t = new Translation(sc);
		t.setInstruction(Instruction.INT);
		t.setOperand(op);
		translation.add(t);
	}
}
