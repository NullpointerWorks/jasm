package com.nullpointerworks.jasm.asm.translator.builder.dataflow;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.CodeTranslator;

public class LoadTranslator implements CodeTranslator
{
	private final String syntax = 	
					"\n  load <reg>, <reg>" + 
					"\n  load <reg>, &<reg>" + 
					"\n  load <reg>, <val>" + 
					"\n  load <reg>, &<val>" + 
					"\n  load &<val>, <reg>" + 
					"\n  load &<reg>, <reg>";
	
	private BuildError error;
	
	public LoadTranslator()
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
		return instruct.equals("load");
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
			error = new TranslationError(sc, "  Syntax error: Load instruction is incomplete."+syntax);
		}
		
		return translation;
	}
	
	private void translate(SourceCode sc, String operands, List<Translation> translation) 
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			error = new TranslationError(sc, "  Syntax error: Load instructions take two arguments."+syntax);
			return;
		}
		
		Operand op1 = new Operand(tokens[0]);
		Operand op2 = new Operand(tokens[1]);
		
		if (op1.isRegister())
		{
			if (op2.isRegister())
			{
				allow(sc, op1, op2, translation);
				return;
			}
			
			if (op2.isNumber()) 
			{
				allow(sc, op1, op2, translation);
				return;
			}
			
			if (op2.isLabel())  // allow only definitions and data references
			{
				allow(sc, op1, op2, translation);
				return;
			}
		}
		
		if (op1.isAddress())
		{
			if (op1.isNumber())
			{
				if (op2.isRegister())
				{
					allow(sc, op1, op2, translation);
					return;
				}
			}
		}
		
		error = new TranslationError(sc, "  Syntax error: Invalid load instruction arguments."+syntax);
	}
	
	private void allow(SourceCode sc, Operand op1, Operand op2, List<Translation> translation) 
	{
		Translation t = new Translation(sc);
		t.setInstruction(ASMInstruction.LOAD);
		t.setOperand(op1);
		t.setOperand(op2);
		translation.add(t);
	}
}
