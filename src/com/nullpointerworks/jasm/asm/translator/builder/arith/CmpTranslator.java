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

public class CmpTranslator implements CodeTranslator
{
	private final String syntax = 	
			"\n  cmp <reg>, <reg>" +
			"\n  cmp <reg>, <val>" +
			"\n  cmp <reg>, <definition>";
	
	private BuildError error;
	
	public CmpTranslator()
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
		return instruct.equals("cmp");
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
			error = new TranslationError(sc, "  Syntax error: CMP instruction is incomplete."+syntax);
		}
		
		return translation;
	}
	
	private void translate(SourceCode sc, String operands, List<Translation> translation) 
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			error = new TranslationError(sc, "  Syntax error: CMP instructions take two arguments."+syntax);
			return;
		}
		
		Operand op1 = new Operand(tokens[0]);
		Operand op2 = new Operand(tokens[1]);
		
		if (!op1.isAddress())
		{
			if (!op2.isAddress())
			{
				noAddress(sc,op1,op2,translation);
				return;
			}
		}
		
		setInvalidArguments(sc);
	}
	
	private void noAddress(SourceCode sc, Operand op1, Operand op2, List<Translation> translation)
	{
		if (op1.isRegister())
		{
			if (op2.isNumber())
			{
				allow(sc, op1, op2, translation);
				return;
			}
			if (op2.isRegister())
			{
				allow(sc, op1, op2, translation);
				return;
			}
			if (op2.isLabel()) // allowed to be a definition
			{
				allow(sc, op1, op2, translation);
				return;
			}
		}
		setInvalidArguments(sc);
	}
	
	private void allow(SourceCode sc, Operand op1, Operand op2, List<Translation> translation) 
	{
		Translation t = new Translation(sc);
		t.setInstruction(ASMInstruction.CMP);
		t.setOperand(op1);
		t.setOperand(op2);
		translation.add(t);
	}
	
	private void setInvalidArguments(SourceCode sc) 
	{
		error = new TranslationError(sc, "  Syntax error: Invalid instruction arguments."+syntax);
	}
}
