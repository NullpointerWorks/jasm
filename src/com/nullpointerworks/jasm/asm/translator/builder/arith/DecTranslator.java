package com.nullpointerworks.jasm.asm.translator.builder.arith;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.CodeTranslator;

public class DecTranslator implements CodeTranslator
{
	private final String syntax = 	
			"\n  dec <reg>";
	
	private BuildError error;
	
	public DecTranslator()
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
		return instruct.equals("dec");
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
			error = new TranslationError(sc, "  Syntax error: DEC instruction is incomplete."+syntax);
		}
		
		return translation;
	}
	
	private void translate(SourceCode sc, String operands, List<Translation> translation) 
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 1) 
		{
			error = new TranslationError(sc, "  Syntax error: DEC instructions take one argument."+syntax);
			return;
		}
		
		Operand op1 = new Operand(tokens[0]);
		if (!op1.isAddress())
		{
			noAddress(sc,op1,translation);
			return;
		}
		
		setInvalidArguments(sc);
	}
	
	private void noAddress(SourceCode sc, Operand op1, List<Translation> translation)
	{
		if (op1.isRegister())
		{
			allow(sc, op1, translation);
			return;
		}
		setInvalidArguments(sc);
	}
	
	private void allow(SourceCode sc, Operand op1, List<Translation> translation) 
	{
		Translation t = new Translation(sc);
		t.setInstruction(ASMInstruction.DEC);
		t.setOperand(op1);
		translation.add(t);
	}
	
	private void setInvalidArguments(SourceCode sc) 
	{
		error = new TranslationError(sc, "  Syntax error: Invalid instruction arguments."+syntax);
	}
}
