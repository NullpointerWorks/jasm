package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.CodeTranslator;

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
		List<Translation> translation = new ArrayList<Translation>();
		
		if (tokens.length == 1)
		{
			translate(sc,translation);
		}
		else
		{
			error = new TranslationError(sc, "  Syntax error: Return calls take no arguments."+syntax);
		}
		
		return translation;
	}
	
	private void translate(SourceCode sc, List<Translation> translation) 
	{
		Translation t = new Translation(sc);
		t.setInstruction(ASMInstruction.RET);
		translation.add(t);
	}
}
