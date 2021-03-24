package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SuperTranslator implements CodeTranslator 
{
	private BuildError error;
	private CodeTranslator sysTranslator;
	private CodeTranslator ctrlTranslator;
	
	public SuperTranslator()
	{
		error = null;
		sysTranslator = new SystemTranslator();
		ctrlTranslator = new ControlFlowTranslator();
		
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
		if (sysTranslator.hasOperation(instruct)) return true;
		if (ctrlTranslator.hasOperation(instruct)) return true;
		return true;
	}

	@Override
	public List<Translation> getTranslation(SourceCode sc)
	{
		String line = sc.getLine();
		String[] tokens = line.split(" ");
		if (tokens.length < 1)
		{
			error = new TranslationError(sc, ""); // TODO
			return null;
		}
		
		String instruct = tokens[0];
		if (instruct.length() < 1)
		{
			error = new TranslationError(sc, ""); // TODO
			return null;
		}
		
		List<Translation> translation = new ArrayList<Translation>();
		checkTranslator(sc, instruct, sysTranslator, translation);
		checkTranslator(sc, instruct, ctrlTranslator, translation);
		
		return translation;
	}
	
	private void checkTranslator(SourceCode sc,
								String instruct,
								CodeTranslator translator, 
								List<Translation> translation)
	{
		if (translator.hasOperation(instruct)) 
		{
			var list = translator.getTranslation(sc);
			if (translator.hasErrors())
			{
				error = translator.getError();
				return;
			}
			for (Translation tr : list) translation.add(tr);
		}
	}

}
