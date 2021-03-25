package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class RetTranslator implements CodeTranslator
{
	private final String syntax = "\n  ret";
	private BuildError error;
	
	public RetTranslator()
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
		return instruct.equals("ret");
	}
	
	@Override
	public List<Translation> getTranslation(SourceCode sc)
	{
		error = null;
		String line = sc.getLine();
		String[] tokens = line.split(" ");
		String operands = "";
		if (tokens.length > 1) operands = tokens[1].toLowerCase();
		List<Translation> translation = new ArrayList<Translation>();
		
		if (tokens.length == 2)
		{
			operands = tokens[1].toLowerCase();
			translate(sc,operands,translation);
		}
		else
		{
			error = new TranslationError(sc, "  Syntax error: Jumps take only one argument."+syntax);
		}
		
		return translation;
	}
	
	private void translate(SourceCode sc, String operand, List<Translation> translation) 
	{
		error = null;
		
		if (operand.contains(","))
		{
			error = new TranslationError(sc, "  Syntax error: Jump instructions only accept one operand."+syntax);
			return;
		}
		
		Operand op = new Operand(operand); // only allowed to be a label
		if (!op.isLabel())
		{
			error = new TranslationError(sc, "  Syntax error: Jumps only accept labels."+syntax);
			return;
		}
		
		Translation t = new Translation(sc);
		t.setInstruction(Instruction.RET);
		t.setOperand(op);
		translation.add(t);
	}
}
