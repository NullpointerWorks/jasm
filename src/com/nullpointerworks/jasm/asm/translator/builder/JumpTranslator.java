package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class JumpTranslator implements CodeTranslator
{
	private final String syntax = "\n  jmp <label>";
	private BuildError error;
	
	public JumpTranslator()
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
		return instruct.equals("jmp");
	}
	
	@Override
	public void translate(SourceCode sc, String operand, List<Translation> translation) 
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
		t.setInstruction(Instruction.JMP);
		t.setOperand(op);
		translation.add(t);
	}
}
