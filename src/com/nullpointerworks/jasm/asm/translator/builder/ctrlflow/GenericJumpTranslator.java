package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.CodeTranslator;

abstract class GenericJumpTranslator implements CodeTranslator
{
	private String syntax = "\n  jmp <label>";
	private String trigger;
	private ASMInstruction inst;
	private BuildError error;
	
	public GenericJumpTranslator()
	{
		error = null;
	}
	
	protected final void initTranslator(String syntax, String trigger, ASMInstruction inst)
	{
		this.syntax = syntax;
		this.trigger = trigger;
		this.inst = inst;
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
		return instruct.equals(trigger);
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
		t.setInstruction(inst);
		t.setOperand(op);
		translation.add(t);
	}
}
