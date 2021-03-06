package com.nullpointerworks.jasm.asm.translator.builder.sys;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.CodeTranslator;

public class NopTranslator implements CodeTranslator
{
	//private final String syntax = "\n  nop";
	private BuildError error;
	
	public NopTranslator()
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
		return instruct.equals("nop");
	}

	@Override
	public List<Translation> getTranslation(SourceCode sc)
	{
		error = null;
		List<Translation> translation = new ArrayList<Translation>();
		translate(sc,translation);
		return translation;
	}
	
	private void translate(SourceCode sc, List<Translation> translation) 
	{
		Translation t = new Translation(sc);
		t.setInstruction(ASMInstruction.NOP);
		translation.add(t);
	}
}
