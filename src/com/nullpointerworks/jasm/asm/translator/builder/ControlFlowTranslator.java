package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class ControlFlowTranslator implements CodeTranslator
{
	private List<Translation> translation;
	private BuildError error;
	
	private final String jmp_syntax = "\n  jmp <label>";
	
	public ControlFlowTranslator()
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
		if (instruct.equals("jmp")) return true;
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
		
		if (instruct.equals("jmp"))
		{
			buildJMP(sc, operands);
		}
		
		return translation;
	}
	
	protected void buildJMP(SourceCode sc, String operand) 
	{
		if (operand.contains(","))
		{
			error = new TranslationError(sc, "  Syntax error: Interrupt instructions only accept one operand."+jmp_syntax);
			return;
		}
		
		Operand op = new Operand(operand); // only allowed to be a label
		if (!op.isNumber())
		{
			if (!op.isLabel())
			{
				error = new TranslationError(sc, "  Syntax error: Interrupts only accept numbers or definitions."+jmp_syntax);
				return;
			}
		}
		if (op.isAddress())
		{
			error = new TranslationError(sc, "  Syntax error: Interrupts do not accept addresses."+jmp_syntax);
			return;
		}
		
		Translation t = new Translation(sc);
		t.setInstruction(Instruction.JMP);
		t.setOperand(op);
		translation.add(t);
	}
}
